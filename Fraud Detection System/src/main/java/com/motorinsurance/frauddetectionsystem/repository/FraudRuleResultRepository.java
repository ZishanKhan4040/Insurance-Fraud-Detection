
package com.motorinsurance.frauddetectionsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.motorinsurance.frauddetectionsystem.entity.FraudRuleResult;
import com.motorinsurance.frauddetectionsystem.entity.FraudRuleResult.RuleName;

public interface FraudRuleResultRepository
        extends JpaRepository<FraudRuleResult, Integer> {

    List<FraudRuleResult> findByEvaluation_EvaluationId(Integer evaluationId);

    List<FraudRuleResult> findByRuleName(RuleName ruleName);

    List<FraudRuleResult> findByTriggeredTrue();

}
