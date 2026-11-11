
package com.motorinsurance.frauddetectionsystem.fraud;

import com.motorinsurance.frauddetectionsystem.entity.Claim;

public interface FraudRule {

    String getRuleName();

    int evaluate(Claim claim);

    boolean isTriggered(Claim claim);

    String getReason(Claim claim);

    String getObservedValue(Claim claim);
}
