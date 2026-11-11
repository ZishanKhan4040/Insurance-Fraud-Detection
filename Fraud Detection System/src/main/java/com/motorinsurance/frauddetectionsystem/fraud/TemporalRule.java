
package com.motorinsurance.frauddetectionsystem.fraud;

import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

import com.motorinsurance.frauddetectionsystem.entity.Claim;

@Component
public class TemporalRule implements FraudRule {

    private static final int MAX_POINTS = 25;
    private static final long THRESHOLD_DAYS = 7;

    @Override
    public String getRuleName() {
        return "TEMPORAL";
    }

    @Override
    public int evaluate(Claim claim) {
        return isTriggered(claim) ? MAX_POINTS : 0;
    }

    @Override
    public boolean isTriggered(Claim claim) {
        if (claim == null
                || claim.getPolicy() == null
                || claim.getPolicy().getStartDate() == null
                || claim.getIncidentDate() == null) {
            return false;
        }

        long days = ChronoUnit.DAYS.between(
                claim.getPolicy().getStartDate(),
                claim.getIncidentDate()
        );

        return days >= 0 && days <= THRESHOLD_DAYS;
    }

    @Override
    public String getReason(Claim claim) {
        if (isTriggered(claim)) {
            return "Incident occurred within 7 days of policy start; review recommended.";
        }

        return "Incident date is outside the configured temporal threshold.";
    }

    @Override
    public String getObservedValue(Claim claim) {
        if (claim == null
                || claim.getPolicy() == null
                || claim.getPolicy().getStartDate() == null
                || claim.getIncidentDate() == null) {
            return "Insufficient data";
        }

        long days = ChronoUnit.DAYS.between(
                claim.getPolicy().getStartDate(),
                claim.getIncidentDate()
        );

        return days + " days after policy start";
    }
}
