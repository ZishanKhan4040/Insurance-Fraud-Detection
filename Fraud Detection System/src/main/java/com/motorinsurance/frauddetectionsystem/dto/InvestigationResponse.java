
package com.motorinsurance.frauddetectionsystem.dto;

import java.time.LocalDateTime;

public class InvestigationResponse {

    private Integer investigationId;
    private Integer claimId;
    private Integer investigatorId;
    private String decision;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;

    public InvestigationResponse() {
    }

    public Integer getInvestigationId() {
        return investigationId;
    }

    public void setInvestigationId(Integer investigationId) {
        this.investigationId = investigationId;
    }

    public Integer getClaimId() {
        return claimId;
    }

    public void setClaimId(Integer claimId) {
        this.claimId = claimId;
    }

    public Integer getInvestigatorId() {
        return investigatorId;
    }

    public void setInvestigatorId(Integer investigatorId) {
        this.investigatorId = investigatorId;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
}
