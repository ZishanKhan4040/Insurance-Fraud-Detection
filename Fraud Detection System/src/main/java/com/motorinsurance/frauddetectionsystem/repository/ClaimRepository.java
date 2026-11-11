
package com.motorinsurance.frauddetectionsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.motorinsurance.frauddetectionsystem.entity.Claim;
import com.motorinsurance.frauddetectionsystem.entity.Claim.ClaimStatus;

public interface ClaimRepository extends JpaRepository<Claim, Integer> {

    List<Claim> findByStatus(ClaimStatus status);

    List<Claim> findByPolicy_PolicyId(Integer policyId);

    List<Claim> findByPolicy_User_UserId(Integer userId);
    
    long countByStatus(ClaimStatus status);

    long countByStatusIn(List<ClaimStatus> statuses);

    long countByPolicy_User_Role(
            com.motorinsurance.frauddetectionsystem.entity.User.Role role);

    long countByPolicy_User_RoleAndStatus(
            com.motorinsurance.frauddetectionsystem.entity.User.Role role,
            ClaimStatus status);

    long countByPolicy_User_RoleAndStatusIn(
            com.motorinsurance.frauddetectionsystem.entity.User.Role role,
            List<ClaimStatus> statuses);

}
