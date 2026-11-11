
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
@Table(name = "fraud_evaluations")
public class FraudEvaluation {

    public enum RiskLevel {
        LOW,
        MEDIUM,
        HIGH
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_id")
    private Integer evaluationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Column(name = "frequency_score", nullable = false)
    private Integer frequencyScore = 0;

    @Column(name = "temporal_score", nullable = false)
    private Integer temporalScore = 0;

    @Column(name = "amount_outlier_score", nullable = false)
    private Integer amountOutlierScore = 0;

    @Column(name = "total_risk_score", nullable = false)
    private Integer totalRiskScore = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false)
    private RiskLevel riskLevel = RiskLevel.LOW;

    @Column(name = "evaluated_at", insertable = false, updatable = false)
    private LocalDateTime evaluatedAt;

    public FraudEvaluation() {
    }

    public Integer getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(Integer evaluationId) {
        this.evaluationId = evaluationId;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
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

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public LocalDateTime getEvaluatedAt() {
        return evaluatedAt;
    }
}
