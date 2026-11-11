
package com.motorinsurance.frauddetectionsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.motorinsurance.frauddetectionsystem.entity.Policy;

public interface PolicyRepository extends JpaRepository<Policy, Integer> {

    Optional<Policy> findByPolicyNumber(String policyNumber);

    boolean existsByPolicyNumber(String policyNumber);

    List<Policy> findByUser_UserId(Integer userId);

    List<Policy> findByStatus(Policy.PolicyStatus status);
}
