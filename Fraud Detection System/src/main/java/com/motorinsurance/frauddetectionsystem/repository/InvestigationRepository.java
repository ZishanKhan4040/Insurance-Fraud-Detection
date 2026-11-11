
package com.motorinsurance.frauddetectionsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.motorinsurance.frauddetectionsystem.entity.Investigation;
import com.motorinsurance.frauddetectionsystem.entity.Investigation.Decision;

public interface InvestigationRepository
        extends JpaRepository<Investigation, Integer> {

    List<Investigation> findByDecision(Decision decision);

    List<Investigation> findByInvestigator_UserId(Integer investigatorId);

    List<Investigation> findByClaim_ClaimId(Integer claimId);
    List<Investigation> findByClaim_Policy_User_UserIdAndDecision(Integer userId, Decision decision);

}
