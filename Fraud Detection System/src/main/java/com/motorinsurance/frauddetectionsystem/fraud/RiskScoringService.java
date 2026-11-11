
package com.motorinsurance.frauddetectionsystem.fraud;

import org.springframework.stereotype.Service;

import com.motorinsurance.frauddetectionsystem.entity.FraudEvaluation.RiskLevel;

@Service
public class RiskScoringService {

    public RiskLevel determineRiskLevel(int totalScore) {

        if (totalScore < 0 || totalScore > 100) {
            throw new IllegalArgumentException(
                    "Total risk score must be between 0 and 100");
        }

        if (totalScore >= 60) {
            return RiskLevel.HIGH;
        } else if (totalScore >= 30) {
            return RiskLevel.MEDIUM;
        } else {
            return RiskLevel.LOW;
        }
    }
}
