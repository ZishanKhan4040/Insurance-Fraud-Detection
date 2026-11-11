
package com.motorinsurance.frauddetectionsystem.dto;

import java.time.LocalDateTime;

public class FraudRuleResultResponse {

    private Integer ruleResultId;
    private String ruleName;
    private Boolean triggered;
    private Integer pointsAwarded;
    private String observedValue;
    private String reason;
    private LocalDateTime createdAt;

    public FraudRuleResultResponse() {
    }

    public Integer getRuleResultId() {
        return ruleResultId;
    }

    public void setRuleResultId(Integer ruleResultId) {
        this.ruleResultId = ruleResultId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
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

    public String getObservedValue() {
        return observedValue;
    }

    public void setObservedValue(String observedValue) {
        this.observedValue = observedValue;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
