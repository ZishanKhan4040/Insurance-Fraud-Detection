
package com.motorinsurance.frauddetectionsystem.dto;

import java.time.LocalDateTime;
import java.util.List;

public class FraudEvaluationResponse {

    private Integer evaluationId;
    private Integer claimId;

    private Integer frequencyScore;
    private Integer temporalScore;
    private Integer amountOutlierScore;

    private Integer totalRiskScore;
    private String riskLevel;

    private LocalDateTime evaluatedAt;

    private List<FraudRuleResultResponse> ruleResults;

    public FraudEvaluationResponse() {
    }

    public Integer getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(Integer evaluationId) {
        this.evaluationId = evaluationId;
    }

    public Integer getClaimId() {
        return claimId;
    }

    public void setClaimId(Integer claimId) {
        this.claimId = claimId;
    }

    public Integer getFrequencyScore() {
        return frequencyScore;
    }

    public void setFrequencyScore(Integer frequencyScore) {
        this.frequencyScore = frequencyScore;
    }

    public Integer getTemporalScore() {
        return temporalScore;
    }

    public void setTemporalScore(Integer temporalScore) {
        this.temporalScore = temporalScore;
    }

    public Integer getAmountOutlierScore() {
        return amountOutlierScore;
    }

    public void setAmountOutlierScore(Integer amountOutlierScore) {
        this.amountOutlierScore = amountOutlierScore;
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

    public List<FraudRuleResultResponse> getRuleResults() {
        return ruleResults;
    }

    public void setRuleResults(List<FraudRuleResultResponse> ruleResults) {
        this.ruleResults = ruleResults;
    }
}
