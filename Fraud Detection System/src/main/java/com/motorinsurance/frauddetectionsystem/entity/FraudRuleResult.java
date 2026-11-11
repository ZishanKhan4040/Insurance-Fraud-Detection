
package com.motorinsurance.frauddetectionsystem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "fraud_rule_results")
public class FraudRuleResult {

    public enum RuleName {
        FREQUENCY,
        TEMPORAL,
        AMOUNT_OUTLIER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_result_id")
    private Integer ruleResultId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evaluation_id", nullable = false)
    private FraudEvaluation evaluation;

    @Column(name = "observed_value", length = 100)
    private String observedValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "rule_name", nullable = false)
    private RuleName ruleName;

    @Column(name = "triggered", nullable = false)
    private Boolean triggered = false;

    @Column(name = "points_awarded", nullable = false)
    private Integer pointsAwarded = 0;

    @Column(name = "reason", nullable = false, length = 500)
    private String reason;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public FraudRuleResult() {
    }

    public Integer getRuleResultId() {
        return ruleResultId;
    }

    public void setRuleResultId(Integer ruleResultId) {
        this.ruleResultId = ruleResultId;
    }

    public FraudEvaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(FraudEvaluation evaluation) {
        this.evaluation = evaluation;
    }

    public String getObservedValue() {
        return observedValue;
    }

    public void setObservedValue(String observedValue) {
        this.observedValue = observedValue;
    }

    public RuleName getRuleName() {
        return ruleName;
    }

    public void setRuleName(RuleName ruleName) {
        this.ruleName = ruleName;
    }

    public Boolean getTriggered() {
        return triggered;
    }

    public void setTriggered(Boolean triggered) {
        this.triggered = triggered;
    }

    public Integer getPointsAwarded() {
        return pointsAwarded;
    }

    public void setPointsAwarded(Integer pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
