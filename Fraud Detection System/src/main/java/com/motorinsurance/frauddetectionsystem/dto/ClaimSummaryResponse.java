
package com.motorinsurance.frauddetectionsystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.motorinsurance.frauddetectionsystem.entity.Claim.ClaimStatus;

public class ClaimSummaryResponse {

    private Integer claimId;
    private String policyNumber;
    private String claimType;
    private BigDecimal claimAmount;
    private LocalDate incidentDate;
    private LocalDateTime submittedAt;
    private ClaimStatus status;

    public ClaimSummaryResponse() {
    }

    public ClaimSummaryResponse(
            Integer claimId,
            String policyNumber,
            String claimType,
            BigDecimal claimAmount,
            LocalDate incidentDate,
            LocalDateTime submittedAt,
            ClaimStatus status) {

        this.claimId = claimId;
        this.policyNumber = policyNumber;
        this.claimType = claimType;
        this.claimAmount = claimAmount;
        this.incidentDate = incidentDate;
        this.submittedAt = submittedAt;
        this.status = status;
    }

    public Integer getClaimId() {
        return claimId;
    }

    public void setClaimId(Integer claimId) {
        this.claimId = claimId;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public BigDecimal getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(BigDecimal claimAmount) {
        this.claimAmount = claimAmount;
    }

    public LocalDate getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(LocalDate incidentDate) {
        this.incidentDate = incidentDate;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }
    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }
}
