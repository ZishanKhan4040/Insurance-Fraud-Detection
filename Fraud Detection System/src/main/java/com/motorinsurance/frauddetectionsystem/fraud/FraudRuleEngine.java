
package com.motorinsurance.frauddetectionsystem.fraud;

import java.util.List;

import org.springframework.stereotype.Component;

import com.motorinsurance.frauddetectionsystem.entity.Claim;

@Component
public class FraudRuleEngine {

    private final List<FraudRule> fraudRules;

    public FraudRuleEngine(List<FraudRule> fraudRules) {
        this.fraudRules = fraudRules;
    }

    public List<FraudRule> getFraudRules() {
        return fraudRules;
    }

    public int calculateTotalScore(Claim claim) {
        return fraudRules.stream()
                .mapToInt(rule -> rule.evaluate(claim))
                .sum();
    }
}
