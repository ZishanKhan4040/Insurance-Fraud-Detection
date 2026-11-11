
package com.motorinsurance.frauddetectionsystem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class InvestigationRequest {

    @NotNull(message = "Claim ID is required")
    private Integer claimId;

    @NotNull(message = "Decision is required")
    private String decision;

    @Size(max = 5000, message = "Remarks cannot exceed 5000 characters")
    private String remarks;

    public InvestigationRequest() {
    }

    public Integer getClaimId() {
        return claimId;
    }

    public void setClaimId(Integer claimId) {
        this.claimId = claimId;
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
}
