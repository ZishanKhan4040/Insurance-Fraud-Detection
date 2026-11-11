
package com.motorinsurance.frauddetectionsystem.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.motorinsurance.frauddetectionsystem.dto.ClaimRequest;
import com.motorinsurance.frauddetectionsystem.dto.ClaimSummaryResponse;
import com.motorinsurance.frauddetectionsystem.entity.Claim;
import com.motorinsurance.frauddetectionsystem.entity.FraudEvaluation;
import com.motorinsurance.frauddetectionsystem.entity.FraudRuleResult;
import com.motorinsurance.frauddetectionsystem.entity.FraudRuleResult.RuleName;
import com.motorinsurance.frauddetectionsystem.entity.Policy;
import com.motorinsurance.frauddetectionsystem.entity.Policy.PolicyStatus;
import com.motorinsurance.frauddetectionsystem.entity.User;
import com.motorinsurance.frauddetectionsystem.fraud.FraudRule;
import com.motorinsurance.frauddetectionsystem.fraud.FraudRuleEngine;
import com.motorinsurance.frauddetectionsystem.fraud.RiskScoringService;
import com.motorinsurance.frauddetectionsystem.repository.ClaimRepository;
import com.motorinsurance.frauddetectionsystem.repository.FraudEvaluationRepository;
import com.motorinsurance.frauddetectionsystem.repository.FraudRuleResultRepository;
import com.motorinsurance.frauddetectionsystem.repository.PolicyRepository;
import com.motorinsurance.frauddetectionsystem.repository.UserRepository;

@Service
public class ClaimService {

    private static final int MAX_FREQUENCY_SCORE = 30;
    private static final int MAX_TEMPORAL_SCORE = 25;
    private static final int MAX_AMOUNT_OUTLIER_SCORE = 45;
    private static final int MAX_TOTAL_SCORE = 100;

    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;
    private final FraudEvaluationRepository fraudEvaluationRepository;
    private final FraudRuleResultRepository fraudRuleResultRepository;
    private final FraudRuleEngine fraudRuleEngine;
    private final RiskScoringService riskScoringService;
    private final UserRepository userRepository;

    public ClaimService(
            ClaimRepository claimRepository,
            PolicyRepository policyRepository,
            FraudEvaluationRepository fraudEvaluationRepository,
            FraudRuleResultRepository fraudRuleResultRepository,
            FraudRuleEngine fraudRuleEngine,
            RiskScoringService riskScoringService,
            UserRepository userRepository) {

        this.claimRepository = claimRepository;
        this.policyRepository = policyRepository;
        this.fraudEvaluationRepository = fraudEvaluationRepository;
        this.fraudRuleResultRepository = fraudRuleResultRepository;
        this.fraudRuleEngine = fraudRuleEngine;
        this.riskScoringService = riskScoringService;
        this.userRepository = userRepository;
    }

    @Transactional
    public FraudEvaluation submitClaim(
            ClaimRequest request, String email) {

        // 1. Validate request.
        if (request == null) {
            throw new IllegalArgumentException(
                    "Claim request is required");
        }

        if (request.getPolicyId() == null) {
            throw new IllegalArgumentException(
                    "Policy ID is required");
        }

        if (request.getClaimType() == null
                || request.getClaimType().isBlank()) {
            throw new IllegalArgumentException(
                    "Claim type is required");
        }

        if (request.getIncidentDate() == null) {
            throw new IllegalArgumentException(
                    "Incident date is required");
        }

        if (request.getClaimAmount() == null
                || request.getClaimAmount()
                        .compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Claim amount must be greater than zero");
        }

        // 2. Find logged-in user.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        // 3. Find and validate policy.
        Policy policy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Policy not found"));

        // Verify policy ownership.
        if (!policy.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException(
                    "You are not authorized to use this policy");
        }

        // Only approved policies can be used.
        if (policy.getStatus() != PolicyStatus.APPROVED) {
            throw new IllegalArgumentException(
                    "Claims can only be submitted against approved policies");
        }

        // Validate claim amount against policy Sum Insured.
        BigDecimal sumInsured = policy.getSumInsured();

        if (sumInsured != null &&
                request.getClaimAmount().compareTo(sumInsured) > 0) {
            throw new IllegalArgumentException(
                    "Claim amount cannot exceed the policy Sum Insured of "
                            + sumInsured);
        }

        LocalDate startDate = policy.getStartDate();
        LocalDate endDate = policy.getEndDate();
        LocalDate incidentDate = request.getIncidentDate();

        if (startDate == null || endDate == null
                || endDate.isBefore(startDate)) {
            throw new IllegalStateException(
                    "Policy validity dates are invalid");
        }

        if (incidentDate.isBefore(startDate)
                || incidentDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "Incident date must be within the policy validity period");
        }

        Claim.ClaimType claimType;

        try {
            claimType = Claim.ClaimType.valueOf(
                    request.getClaimType().trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Invalid claim type. Allowed values: ACCIDENT, THEFT, FIRE, NATURAL_CALAMITY");
        }

        // 4. Create and save the claim.
        Claim claim = new Claim();
        claim.setPolicy(policy);
        claim.setClaimType(claimType);
        claim.setIncidentDate(incidentDate);
        claim.setClaimAmount(request.getClaimAmount());
        claim.setDescription(request.getDescription());
        claim.setStatus(Claim.ClaimStatus.SUBMITTED);

        claim = claimRepository.saveAndFlush(claim);

        // 5. Evaluate configured fraud rules.
        int frequencyScore = 0;
        int temporalScore = 0;
        int amountOutlierScore = 0;

        List<FraudRuleResult> results = new ArrayList<>();

        EnumSet<RuleName> evaluatedRules =
                EnumSet.noneOf(RuleName.class);

        for (FraudRule rule : fraudRuleEngine.getFraudRules()) {

            RuleName ruleName;

            try {
                ruleName = RuleName.valueOf(rule.getRuleName());
            } catch (IllegalArgumentException | NullPointerException ex) {
                throw new IllegalStateException(
                        "Unknown fraud rule: " + rule.getRuleName());
            }

            if (!evaluatedRules.add(ruleName)) {
                throw new IllegalStateException(
                        "Duplicate fraud rule configured: " + ruleName);
            }

            int points = rule.evaluate(claim);
            boolean triggered = rule.isTriggered(claim);

            if (points < 0 || points > MAX_TOTAL_SCORE) {
                throw new IllegalStateException(
                        "Fraud rule points must be between 0 and 100: "
                                + ruleName);
            }

            if (triggered != (points > 0)) {
                throw new IllegalStateException(
                        "Fraud rule trigger status and points are inconsistent: "
                                + ruleName);
            }

            FraudRuleResult result = new FraudRuleResult();
            result.setRuleName(ruleName);
            result.setTriggered(triggered);
            result.setPointsAwarded(points);
            result.setReason(rule.getReason(claim));
            result.setObservedValue(rule.getObservedValue(claim));

            if (result.getReason() == null
                    || result.getReason().isBlank()) {
                throw new IllegalStateException(
                        "Fraud rule reason is required: " + ruleName);
            }

            if (result.getReason().length() > 500) {
                throw new IllegalStateException(
                        "Fraud rule reason exceeds 500 characters: "
                                + ruleName);
            }

            if (result.getObservedValue() != null
                    && result.getObservedValue().length() > 100) {
                throw new IllegalStateException(
                        "Fraud rule observed value exceeds 100 characters: "
                                + ruleName);
            }

            results.add(result);

            switch (ruleName) {

                case FREQUENCY:
                    if (points > MAX_FREQUENCY_SCORE) {
                        throw new IllegalStateException(
                                "Frequency score exceeds 30");
                    }
                    frequencyScore = points;
                    break;

                case TEMPORAL:
                    if (points > MAX_TEMPORAL_SCORE) {
                        throw new IllegalStateException(
                                "Temporal score exceeds 25");
                    }
                    temporalScore = points;
                    break;

                case AMOUNT_OUTLIER:
                    if (points > MAX_AMOUNT_OUTLIER_SCORE) {
                        throw new IllegalStateException(
                                "Amount outlier score exceeds 45");
                    }
                    amountOutlierScore = points;
                    break;
            }
        }

        // 6. Require all three fraud rules.
        if (evaluatedRules.size() != 3
                || !evaluatedRules.containsAll(
                        EnumSet.allOf(RuleName.class))) {
            throw new IllegalStateException(
                    "All three fraud rules must be configured");
        }

        // 7. Calculate total risk score.
        int totalScore =
                frequencyScore + temporalScore + amountOutlierScore;

        if (totalScore < 0 || totalScore > MAX_TOTAL_SCORE) {
            throw new IllegalStateException(
                    "Total fraud risk score must be between 0 and 100");
        }

        int sumOfRulePoints = results.stream()
                .mapToInt(FraudRuleResult::getPointsAwarded)
                .sum();

        if (totalScore != sumOfRulePoints) {
            throw new IllegalStateException(
                    "Total score does not match individual rule scores");
        }

        // 8. Save fraud evaluation.
        FraudEvaluation evaluation = new FraudEvaluation();
        evaluation.setClaim(claim);
        evaluation.setFrequencyScore(frequencyScore);
        evaluation.setTemporalScore(temporalScore);
        evaluation.setAmountOutlierScore(amountOutlierScore);
        evaluation.setTotalRiskScore(totalScore);
        evaluation.setRiskLevel(
                riskScoringService.determineRiskLevel(totalScore));

        evaluation = fraudEvaluationRepository.saveAndFlush(evaluation);

        // 9. Save individual rule results.
        for (FraudRuleResult result : results) {
            result.setEvaluation(evaluation);
        }

        fraudRuleResultRepository.saveAll(results);

        return evaluation;
    }

    // Fetch only claims belonging to the logged-in claimant.
    @Transactional(readOnly = true)
    public List<ClaimSummaryResponse> getMyClaims(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        List<Claim> claims =
                claimRepository.findByPolicy_User_UserId(user.getUserId());

        return claims.stream()
                .sorted(Comparator.comparing(
                        Claim::getSubmittedAt,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(claim -> new ClaimSummaryResponse(
                        claim.getClaimId(),
                        claim.getPolicy().getPolicyNumber(),
                        claim.getClaimType().name(),
                        claim.getClaimAmount(),
                        claim.getIncidentDate(),
                        claim.getSubmittedAt(),
                        claim.getStatus()))
                .toList();
    }
}
