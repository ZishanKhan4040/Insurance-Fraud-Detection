
package com.motorinsurance.frauddetectionsystem.dto;

public class InvestigatorDashboardStats {

    private long totalClaims;
    private long pendingClaims;
    private long approvedClaims;
    private long rejectedClaims;
    private long moreInfoRequiredClaims;

    public InvestigatorDashboardStats() {
    }

    public InvestigatorDashboardStats(
            long totalClaims,
            long pendingClaims,
            long approvedClaims,
            long rejectedClaims,
            long moreInfoRequiredClaims) {

        this.totalClaims = totalClaims;
        this.pendingClaims = pendingClaims;
        this.approvedClaims = approvedClaims;
        this.rejectedClaims = rejectedClaims;
        this.moreInfoRequiredClaims = moreInfoRequiredClaims;
    }

    public long getTotalClaims() {
        return totalClaims;
    }

    public long getPendingClaims() {
        return pendingClaims;
    }

    public long getApprovedClaims() {
        return approvedClaims;
    }

    public long getRejectedClaims() {
        return rejectedClaims;
    }

    public long getMoreInfoRequiredClaims() {
        return moreInfoRequiredClaims;
    }
}
