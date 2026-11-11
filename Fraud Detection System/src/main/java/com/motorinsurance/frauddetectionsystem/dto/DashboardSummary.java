
package com.motorinsurance.frauddetectionsystem.dto;

public class DashboardSummary {

    private Long totalClaims;
    private Long pendingClaims;
    private Long totalEvaluations;
    private Long lowRiskClaims;
    private Long mediumRiskClaims;
    private Long highRiskClaims;
    private Long pendingInvestigations;

    public DashboardSummary() {
    }

    public Long getTotalClaims() {
        return totalClaims;
    }

    public void setTotalClaims(Long totalClaims) {
        this.totalClaims = totalClaims;
    }

    public Long getPendingClaims() {
        return pendingClaims;
    }

    public void setPendingClaims(Long pendingClaims) {
        this.pendingClaims = pendingClaims;
    }

    public Long getTotalEvaluations() {
        return totalEvaluations;
    }

    public void setTotalEvaluations(Long totalEvaluations) {
        this.totalEvaluations = totalEvaluations;
    }

    public Long getLowRiskClaims() {
        return lowRiskClaims;
    }

    public void setLowRiskClaims(Long lowRiskClaims) {
        this.lowRiskClaims = lowRiskClaims;
    }

    public Long getMediumRiskClaims() {
        return mediumRiskClaims;
    }

    public void setMediumRiskClaims(Long mediumRiskClaims) {
        this.mediumRiskClaims = mediumRiskClaims;
    }

    public Long getHighRiskClaims() {
        return highRiskClaims;
    }

    public void setHighRiskClaims(Long highRiskClaims) {
        this.highRiskClaims = highRiskClaims;
    }

    public Long getPendingInvestigations() {
        return pendingInvestigations;
    }

    public void setPendingInvestigations(Long pendingInvestigations) {
        this.pendingInvestigations = pendingInvestigations;
    }
}
