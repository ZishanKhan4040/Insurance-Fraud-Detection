
package com.motorinsurance.frauddetectionsystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class InvestigatorClaimDetails {

    private Integer claimId;
    private String claimantName;
    private String claimantEmail;

    private String policyNumber;
    private String vehicleType;
    private String policyType;
    private LocalDate policyStartDate;
    private LocalDate policyEndDate;

    private String claimType;
    private LocalDate incidentDate;
    private BigDecimal claimAmount;
    private String description;
    private String claimStatus;

    private Integer totalRiskScore;
    private String riskLevel;
    private LocalDateTime evaluatedAt;

    private List<FraudRuleDetails> fraudRules;

    public InvestigatorClaimDetails() {
    }

    public Integer getClaimId() {
        return claimId;
    }

    public void setClaimId(Integer claimId) {
        this.claimId = claimId;
    }

    public String getClaimantName() {
        return claimantName;
    }

    public void setClaimantName(String claimantName) {
        this.claimantName = claimantName;
    }

    public String getClaimantEmail() {
        return claimantEmail;
    }

    public void setClaimantEmail(String claimantEmail) {
        this.claimantEmail = claimantEmail;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public LocalDate getPolicyStartDate() {
        return policyStartDate;
    }

    public void setPolicyStartDate(LocalDate policyStartDate) {
        this.policyStartDate = policyStartDate;
    }

    public LocalDate getPolicyEndDate() {
        return policyEndDate;
    }

    public void setPolicyEndDate(LocalDate policyEndDate) {
        this.policyEndDate = policyEndDate;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public LocalDate getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(LocalDate incidentDate) {
        this.incidentDate = incidentDate;
    }

    public BigDecimal getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(BigDecimal claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public Integer getTotalRiskScore() {
        return totalRiskScore;
    }

    public void setTotalRiskScore(Integer totalRiskScore) {
        this.totalRiskScore = totalRiskScore;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public LocalDateTime getEvaluatedAt() {
        return evaluatedAt;
    }

    public void setEvaluatedAt(LocalDateTime evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
    }

    public List<FraudRuleDetails> getFraudRules() {
        return fraudRules;
    }

    public void setFraudRules(List<FraudRuleDetails> fraudRules) {
        this.fraudRules = fraudRules;
    }

    public static class FraudRuleDetails {

        private String ruleName;
        private Boolean triggered;
        private String observedValue;
        private Integer pointsAwarded;
        private String reason;

        public FraudRuleDetails() {
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

        public String getObservedValue() {
            return observedValue;
        }

        public void setObservedValue(String observedValue) {
            this.observedValue = observedValue;
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
    }
}
