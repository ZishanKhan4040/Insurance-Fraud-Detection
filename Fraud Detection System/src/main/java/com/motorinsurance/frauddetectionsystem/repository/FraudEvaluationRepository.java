
package com.motorinsurance.frauddetectionsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.motorinsurance.frauddetectionsystem.entity.FraudEvaluation;
import com.motorinsurance.frauddetectionsystem.entity.FraudEvaluation.RiskLevel;

public interface FraudEvaluationRepository
        extends JpaRepository<FraudEvaluation, Integer> {

    Optional<FraudEvaluation> findByClaim_ClaimId(Integer claimId);

    List<FraudEvaluation> findByRiskLevel(RiskLevel riskLevel);

    List<FraudEvaluation> findByTotalRiskScoreGreaterThanEqual(Integer score);

    List<FraudEvaluation> findByClaim_Policy_User_UserId(Integer userId);

    List<FraudEvaluation> findByClaim_Policy_User_UserIdAndRiskLevel(
        Integer userId, RiskLevel riskLevel);
}
