
package com.motorinsurance.frauddetectionsystem.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.motorinsurance.frauddetectionsystem.dto.FraudEvaluationResponse;
import com.motorinsurance.frauddetectionsystem.dto.FraudRuleResultResponse;
import com.motorinsurance.frauddetectionsystem.entity.FraudEvaluation;
import com.motorinsurance.frauddetectionsystem.entity.FraudRuleResult;
import com.motorinsurance.frauddetectionsystem.entity.User;
import com.motorinsurance.frauddetectionsystem.repository.FraudEvaluationRepository;
import com.motorinsurance.frauddetectionsystem.repository.FraudRuleResultRepository;
import com.motorinsurance.frauddetectionsystem.repository.UserRepository;

@Service
public class FraudEvaluationService {

    private final FraudEvaluationRepository evaluationRepository;
    private final FraudRuleResultRepository ruleResultRepository;
    private final UserRepository userRepository;

    public FraudEvaluationService(
            FraudEvaluationRepository evaluationRepository,
            FraudRuleResultRepository ruleResultRepository,
            UserRepository userRepository) {

        this.evaluationRepository = evaluationRepository;
        this.ruleResultRepository = ruleResultRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public FraudEvaluationResponse getEvaluation(
            Integer claimId, String email) {

        // 1. Find the logged-in user.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        // 2. Find the fraud evaluation for the claim.
        FraudEvaluation evaluation =
                evaluationRepository.findByClaim_ClaimId(claimId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Fraud evaluation not found for this claim"));

        // 3. Verify claim ownership.
        Integer ownerId = evaluation.getClaim()
                .getPolicy()
                .getUser()
                .getUserId();

        if (!ownerId.equals(user.getUserId())) {
            throw new IllegalArgumentException(
                    "You are not authorized to access this evaluation");
        }

        // 4. Fetch individual fraud rule results.
        List<FraudRuleResult> results =
                ruleResultRepository.findByEvaluation_EvaluationId(
                        evaluation.getEvaluationId());

        // 5. Convert rule result entities into DTOs.
        List<FraudRuleResultResponse> ruleResponses =
                results.stream()
                .sorted(Comparator.comparing(
                        result -> result.getRuleName().name()))
                .map(result -> {

                    FraudRuleResultResponse response =
                            new FraudRuleResultResponse();

                    response.setRuleResultId(
                            result.getRuleResultId());

                    response.setRuleName(
                            result.getRuleName().name());

                    response.setTriggered(
                            result.getTriggered());

                    response.setPointsAwarded(
                            result.getPointsAwarded());

                    response.setObservedValue(
                            result.getObservedValue());

                    response.setReason(
                            result.getReason());

                    response.setCreatedAt(
                            result.getCreatedAt());

                    return response;
                })
                .toList();

        // 6. Convert evaluation entity into DTO.
        FraudEvaluationResponse response =
                new FraudEvaluationResponse();

        response.setEvaluationId(
                evaluation.getEvaluationId());

        response.setClaimId(
                evaluation.getClaim().getClaimId());

        response.setFrequencyScore(
                evaluation.getFrequencyScore());

        response.setTemporalScore(
                evaluation.getTemporalScore());

        response.setAmountOutlierScore(
                evaluation.getAmountOutlierScore());

        response.setTotalRiskScore(
                evaluation.getTotalRiskScore());

        response.setRiskLevel(
                evaluation.getRiskLevel().name());

        response.setEvaluatedAt(
                evaluation.getEvaluatedAt());

        response.setRuleResults(ruleResponses);

        return response;
    }
}
