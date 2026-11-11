
package com.motorinsurance.frauddetectionsystem.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.motorinsurance.frauddetectionsystem.dto.DashboardSummary;
import com.motorinsurance.frauddetectionsystem.entity.Claim;
import com.motorinsurance.frauddetectionsystem.entity.Claim.ClaimStatus;
import com.motorinsurance.frauddetectionsystem.entity.FraudEvaluation;
import com.motorinsurance.frauddetectionsystem.entity.FraudEvaluation.RiskLevel;
import com.motorinsurance.frauddetectionsystem.entity.Investigation;
import com.motorinsurance.frauddetectionsystem.entity.Investigation.Decision;
import com.motorinsurance.frauddetectionsystem.entity.User;
import com.motorinsurance.frauddetectionsystem.repository.ClaimRepository;
import com.motorinsurance.frauddetectionsystem.repository.FraudEvaluationRepository;
import com.motorinsurance.frauddetectionsystem.repository.InvestigationRepository;
import com.motorinsurance.frauddetectionsystem.repository.UserRepository;

@Service
public class DashboardService {

    private final ClaimRepository claimRepository;
    private final FraudEvaluationRepository fraudEvaluationRepository;
    private final InvestigationRepository investigationRepository;
    private final UserRepository userRepository;

    public DashboardService(
            ClaimRepository claimRepository,
            FraudEvaluationRepository fraudEvaluationRepository,
            InvestigationRepository investigationRepository,
            UserRepository userRepository) {
        this.claimRepository = claimRepository;
        this.fraudEvaluationRepository = fraudEvaluationRepository;
        this.investigationRepository = investigationRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public DashboardSummary getDashboardSummary(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        Integer userId = user.getUserId();

        List<Claim> claims =
                claimRepository.findByPolicy_User_UserId(userId);

        List<FraudEvaluation> evaluations =
                fraudEvaluationRepository
                        .findByClaim_Policy_User_UserId(userId);

        List<Investigation> investigations =
                investigationRepository
                        .findByClaim_Policy_User_UserIdAndDecision(
                                userId, Decision.PENDING);

        DashboardSummary summary = new DashboardSummary();

        summary.setTotalClaims((long) claims.size());

        summary.setPendingClaims(
                claims.stream()
                        .filter(claim ->
                                claim.getStatus() == ClaimStatus.SUBMITTED)
                        .count());

        summary.setTotalEvaluations((long) evaluations.size());

        summary.setLowRiskClaims(
                evaluations.stream()
                        .filter(evaluation ->
                                evaluation.getRiskLevel() == RiskLevel.LOW)
                        .count());

        summary.setMediumRiskClaims(
                evaluations.stream()
                        .filter(evaluation ->
                                evaluation.getRiskLevel() == RiskLevel.MEDIUM)
                        .count());

        summary.setHighRiskClaims(
                evaluations.stream()
                        .filter(evaluation ->
                                evaluation.getRiskLevel() == RiskLevel.HIGH)
                        .count());

        summary.setPendingInvestigations(
                (long) investigations.size());

        return summary;
    }
}
