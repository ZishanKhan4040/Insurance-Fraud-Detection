
package com.motorinsurance.frauddetectionsystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PolicySummaryResponse {

    private Integer policyId;
    private String policyNumber;
    private String vehicleType;
    private String policyType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer durationYears;
    private BigDecimal sumInsured;
    private BigDecimal premiumAmount;
    private String status;

    public PolicySummaryResponse() {
    }

    public PolicySummaryResponse(
            Integer policyId,
            String policyNumber,
            String vehicleType,
            String policyType,
            LocalDate startDate,
            LocalDate endDate,
            Integer durationYears,
            BigDecimal sumInsured,
            BigDecimal premiumAmount,
            String status) {

        this.policyId = policyId;
        this.policyNumber = policyNumber;
        this.vehicleType = vehicleType;
        this.policyType = policyType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.durationYears = durationYears;
        this.sumInsured = sumInsured;
        this.premiumAmount = premiumAmount;
        this.status = status;
    }

    public Integer getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Integer policyId) {
        this.policyId = policyId;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getDurationYears() {
        return durationYears;
    }

    public void setDurationYears(Integer durationYears) {
        this.durationYears = durationYears;
    }

    public BigDecimal getSumInsured() {
        return sumInsured;
    }

    public void setSumInsured(BigDecimal sumInsured) {
        this.sumInsured = sumInsured;
    }

    public BigDecimal getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(BigDecimal premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
