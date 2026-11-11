
package com.motorinsurance.frauddetectionsystem.fraud;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.motorinsurance.frauddetectionsystem.entity.Claim;
import com.motorinsurance.frauddetectionsystem.repository.ClaimRepository;

@Component
public class CategoryOutlierRule implements FraudRule {

    private static final int MAX_POINTS = 45;
    private static final BigDecimal OUTLIER_MULTIPLIER =
            new BigDecimal("4.0");

    private final ClaimRepository claimRepository;

    public CategoryOutlierRule(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    @Override
    public String getRuleName() {
        return "AMOUNT_OUTLIER";
    }

    @Override
    public int evaluate(Claim claim) {
        return isTriggered(claim) ? MAX_POINTS : 0;
    }

    @Override
    public boolean isTriggered(Claim claim) {
        if (!hasRequiredData(claim)) {
            return false;
        }

        BigDecimal average = calculateCategoryAverage(claim);

        if (average == null || average.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        BigDecimal threshold = average.multiply(OUTLIER_MULTIPLIER);

        return claim.getClaimAmount().compareTo(threshold) > 0;
    }

    @Override
    public String getReason(Claim claim) {
        if (!hasRequiredData(claim)) {
            return "Insufficient claim data to evaluate amount outlier.";
        }

        BigDecimal average = calculateCategoryAverage(claim);

        if (average == null || average.compareTo(BigDecimal.ZERO) <= 0) {
            return "Insufficient historical data for amount comparison.";
        }

        BigDecimal threshold = average.multiply(OUTLIER_MULTIPLIER);

        if (claim.getClaimAmount().compareTo(threshold) > 0) {
            return "Claim amount exceeds four times the historical category average.";
        }

        return "Claim amount is within the configured category threshold.";
    }

    @Override
    public String getObservedValue(Claim claim) {
        if (!hasRequiredData(claim)) {
            return "Insufficient data";
        }

        BigDecimal average = calculateCategoryAverage(claim);

        if (average == null || average.compareTo(BigDecimal.ZERO) <= 0) {
            return "Historical average unavailable";
        }

        BigDecimal multiplier = claim.getClaimAmount()
                .divide(average, 2, java.math.RoundingMode.HALF_UP);

        return multiplier.toPlainString() + "x category average";
    }

    private boolean hasRequiredData(Claim claim) {
        return claim != null
                && claim.getClaimType() != null
                && claim.getClaimAmount() != null
                && claim.getClaimAmount().compareTo(BigDecimal.ZERO) > 0;
    }

    private BigDecimal calculateCategoryAverage(Claim currentClaim) {
        List<Claim> claims = claimRepository.findAll();

        List<Claim> comparableClaims = claims.stream()
                .filter(c -> c.getClaimType() == currentClaim.getClaimType())
                .filter(c -> c.getClaimAmount() != null)
                .filter(c -> c.getClaimAmount().compareTo(BigDecimal.ZERO) > 0)
                .filter(c -> !c.equals(currentClaim))
                .toList();

        if (comparableClaims.isEmpty()) {
            return null;
        }

        BigDecimal total = comparableClaims.stream()
                .map(Claim::getClaimAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(
                BigDecimal.valueOf(comparableClaims.size()),
                2,
                java.math.RoundingMode.HALF_UP
        );
    }
}
