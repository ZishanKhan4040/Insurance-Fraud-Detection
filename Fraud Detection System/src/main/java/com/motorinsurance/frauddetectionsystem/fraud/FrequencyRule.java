
package com.motorinsurance.frauddetectionsystem.fraud;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.motorinsurance.frauddetectionsystem.entity.Claim;
import com.motorinsurance.frauddetectionsystem.repository.ClaimRepository;

@Component
public class FrequencyRule implements FraudRule {

    private static final int MAX_POINTS = 30;
    private static final int CLAIM_THRESHOLD = 3;
    private static final int LOOKBACK_DAYS = 30;

    private final ClaimRepository claimRepository;

    public FrequencyRule(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    @Override
    public String getRuleName() {
        return "FREQUENCY";
    }

    @Override
    public int evaluate(Claim claim) {
        return isTriggered(claim) ? MAX_POINTS : 0;
    }

    @Override
    public boolean isTriggered(Claim claim) {
        if (claim.getPolicy() == null
                || claim.getPolicy().getUser() == null
                || claim.getIncidentDate() == null) {
            return false;
        }

        Integer userId = claim.getPolicy().getUser().getUserId();

        if (userId == null) {
            return false;
        }

        List<Claim> claims =
                claimRepository.findByPolicy_User_UserId(userId);

        LocalDate startDate =
                claim.getIncidentDate().minusDays(LOOKBACK_DAYS);

        long count = claims.stream()
                .filter(c -> c.getIncidentDate() != null)
                .filter(c -> !c.getIncidentDate().isBefore(startDate))
                .filter(c -> !c.getIncidentDate()
                        .isAfter(claim.getIncidentDate()))
                .count();

        // Include the current claim if it has not been saved yet.
        boolean currentClaimIncluded = claims.stream()
                .anyMatch(c -> claim.equals(c));

        if (!currentClaimIncluded) {
            count++;
        }

        return count >= CLAIM_THRESHOLD;
    }

    @Override
    public String getReason(Claim claim) {
        if (!isTriggered(claim)) {
            return "Claim frequency is within the configured threshold.";
        }

        return "Three or more claims detected within a 30-day period.";
    }

    @Override
    public String getObservedValue(Claim claim) {
        if (claim.getPolicy() == null
                || claim.getPolicy().getUser() == null
                || claim.getIncidentDate() == null
                || claim.getPolicy().getUser().getUserId() == null) {
            return "Insufficient data";
        }

        List<Claim> claims = claimRepository.findByPolicy_User_UserId(
                claim.getPolicy().getUser().getUserId());

        LocalDate startDate = claim.getIncidentDate().minusDays(LOOKBACK_DAYS);

        long count = claims.stream()
                .filter(c -> c.getIncidentDate() != null)
                .filter(c -> !c.getIncidentDate().isBefore(startDate))
                .filter(c -> !c.getIncidentDate()
                        .isAfter(claim.getIncidentDate()))
                .count();

        if (claims.stream().noneMatch(c -> claim.equals(c))) {
            count++;
        }

        return count + " claims in 30 days";
    }
}
