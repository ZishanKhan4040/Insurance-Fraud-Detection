
package com.motorinsurance.frauddetectionsystem.service;

import java.time.LocalDateTime;
import java.util.List;
import com.motorinsurance.frauddetectionsystem.repository.FraudRuleResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.motorinsurance.frauddetectionsystem.dto.InvestigationRequest;
import com.motorinsurance.frauddetectionsystem.entity.Claim;
import com.motorinsurance.frauddetectionsystem.entity.Investigation;
import com.motorinsurance.frauddetectionsystem.entity.User;
import com.motorinsurance.frauddetectionsystem.repository.ClaimRepository;
import com.motorinsurance.frauddetectionsystem.repository.InvestigationRepository;
import com.motorinsurance.frauddetectionsystem.repository.UserRepository;

import com.motorinsurance.frauddetectionsystem.dto.InvestigatorDashboardStats;
import java.util.Arrays;
import com.motorinsurance.frauddetectionsystem.entity.Claim.ClaimStatus;

import com.motorinsurance.frauddetectionsystem.repository.FraudEvaluationRepository;
import com.motorinsurance.frauddetectionsystem.entity.FraudEvaluation;
import com.motorinsurance.frauddetectionsystem.dto.InvestigatorClaimDetails;


@Service
public class InvestigationService {
	private final FraudEvaluationRepository fraudEvaluationRepository;
    private final InvestigationRepository investigationRepository;
    private final ClaimRepository claimRepository;
    private final UserRepository userRepository;
    private final FraudRuleResultRepository fraudRuleResultRepository;

public InvestigationService(
        InvestigationRepository investigationRepository,
        ClaimRepository claimRepository,
        UserRepository userRepository,
        FraudRuleResultRepository fraudRuleResultRepository,
        FraudEvaluationRepository fraudEvaluationRepository) {

    this.investigationRepository = investigationRepository;
    this.claimRepository = claimRepository;
    this.userRepository = userRepository;
    this.fraudRuleResultRepository = fraudRuleResultRepository;
    this.fraudEvaluationRepository = fraudEvaluationRepository;
}

    @Transactional
    public Investigation createInvestigation(
            InvestigationRequest request, Integer investigatorId) {

        User investigator = userRepository.findById(investigatorId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Investigator not found"));

        if (investigator.getRole() != User.Role.INVESTIGATOR) {
            throw new IllegalArgumentException(
                    "Only investigators can review claims");
        }

        Claim claim = claimRepository.findById(request.getClaimId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Claim not found"));

        Investigation investigation = new Investigation();
        investigation.setClaim(claim);
        investigation.setInvestigator(investigator);
        investigation.setDecision(
                Investigation.Decision.valueOf(request.getDecision()));
        investigation.setRemarks(request.getRemarks());

        if (investigation.getDecision()
                != Investigation.Decision.PENDING) {
            investigation.setReviewedAt(LocalDateTime.now());
        }
        switch (investigation.getDecision()) {
        case APPROVED:
            claim.setStatus(Claim.ClaimStatus.APPROVED);
            break;

        case REJECTED:
            claim.setStatus(Claim.ClaimStatus.REJECTED);
            break;

        case REQUEST_MORE_REVIEW:
            claim.setStatus(Claim.ClaimStatus.MORE_INFO_REQUIRED);
            break;

        case PENDING:
            claim.setStatus(Claim.ClaimStatus.UNDER_REVIEW);
            break;
    }

        claimRepository.saveAndFlush(claim);

        return investigationRepository.save(investigation);
    }

    @Transactional(readOnly = true)
    public List<Claim> getPendingClaims() {
        return claimRepository.findByStatus(Claim.ClaimStatus.SUBMITTED);
    }
    
    @Transactional(readOnly = true)
    public List<Investigation> getInvestigationsByDecision(
            Investigation.Decision decision) {
        return investigationRepository.findByDecision(decision);
    }

    @Transactional(readOnly = true)
    public List<Investigation> getInvestigationsByClaimId(Integer claimId) {
        return investigationRepository.findByClaim_ClaimId(claimId);
    }


    @Transactional(readOnly = true)
    public InvestigatorDashboardStats getDashboardStats() {

        User.Role role = User.Role.CLAIMANT;

        long totalClaims =
                claimRepository.countByPolicy_User_Role(role);

        long pendingClaims =
                claimRepository.countByPolicy_User_RoleAndStatusIn(
                        role,
                        Arrays.asList(
                                ClaimStatus.SUBMITTED,
                                ClaimStatus.UNDER_REVIEW
                        )
                );

        long approvedClaims =
                claimRepository.countByPolicy_User_RoleAndStatus(
                        role, ClaimStatus.APPROVED
                );

        long rejectedClaims =
                claimRepository.countByPolicy_User_RoleAndStatus(
                        role, ClaimStatus.REJECTED
                );

        long moreInfoRequiredClaims =
                claimRepository.countByPolicy_User_RoleAndStatus(
                        role, ClaimStatus.MORE_INFO_REQUIRED
                );

        return new InvestigatorDashboardStats(
                totalClaims,
                pendingClaims,
                approvedClaims,
                rejectedClaims,
                moreInfoRequiredClaims
        );
    }

@Transactional(readOnly = true)
public InvestigatorClaimDetails getClaimDetails(Integer claimId) {

    Claim claim = claimRepository.findById(claimId)
            .orElseThrow(() ->
                    new IllegalArgumentException("Claim not found"));

    FraudEvaluation evaluation =
            fraudEvaluationRepository.findByClaim_ClaimId(claimId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Fraud evaluation not found"));

    InvestigatorClaimDetails details =
            new InvestigatorClaimDetails();

    // Claim details
    details.setClaimId(claim.getClaimId());
    details.setClaimType(claim.getClaimType().name());
    details.setIncidentDate(claim.getIncidentDate());
    details.setClaimAmount(claim.getClaimAmount());
    details.setDescription(claim.getDescription());
    details.setClaimStatus(claim.getStatus().name());

    // Claimant details
    User claimant = claim.getPolicy().getUser();

    details.setClaimantName(claimant.getFullName());
    details.setClaimantEmail(claimant.getEmail());

    // Policy details
    details.setPolicyNumber(claim.getPolicy().getPolicyNumber());
    details.setVehicleType(claim.getPolicy().getVehicleType().name());
    details.setPolicyType(claim.getPolicy().getPolicyType().name());
    details.setPolicyStartDate(claim.getPolicy().getStartDate());
    details.setPolicyEndDate(claim.getPolicy().getEndDate());

    // Overall fraud evaluation
    details.setTotalRiskScore(evaluation.getTotalRiskScore());
    details.setRiskLevel(evaluation.getRiskLevel().name());
    details.setEvaluatedAt(evaluation.getEvaluatedAt());

    // Individual fraud rule results
    List<InvestigatorClaimDetails.FraudRuleDetails> rules =
            fraudRuleResultRepository.findByEvaluation_EvaluationId(
                    evaluation.getEvaluationId())
                    .stream()
                    .map(result -> {
                        InvestigatorClaimDetails.FraudRuleDetails rule =
                                new InvestigatorClaimDetails.FraudRuleDetails();

                        rule.setRuleName(result.getRuleName().name());
                        rule.setTriggered(result.getTriggered());
                        rule.setObservedValue(result.getObservedValue());
                        rule.setPointsAwarded(result.getPointsAwarded());
                        rule.setReason(result.getReason());

                        return rule;
                    })
                    .toList();

    details.setFraudRules(rules);
    System.out.println("CLAIM DETAILS SERVICE COMPLETED SUCCESSFULLY");
    return details;
}


}
