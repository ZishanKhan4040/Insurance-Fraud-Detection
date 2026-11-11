
package com.motorinsurance.frauddetectionsystem.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.motorinsurance.frauddetectionsystem.dto.PolicySummaryResponse;
import com.motorinsurance.frauddetectionsystem.entity.Policy;
import com.motorinsurance.frauddetectionsystem.entity.Policy.PolicyStatus;
import com.motorinsurance.frauddetectionsystem.entity.User;
import com.motorinsurance.frauddetectionsystem.repository.PolicyRepository;
import com.motorinsurance.frauddetectionsystem.repository.UserRepository;

@Service
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final UserRepository userRepository;

    public PolicyService(
            PolicyRepository policyRepository,
            UserRepository userRepository) {

        this.policyRepository = policyRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Policy getPolicyById(Integer policyId) {

        return policyRepository.findById(policyId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Policy not found"));
    }

    @Transactional(readOnly = true)
    public Policy getPolicyByNumber(String policyNumber) {

        return policyRepository.findByPolicyNumber(policyNumber)
                .orElseThrow(() ->
                        new IllegalArgumentException("Policy not found"));
    }

    @Transactional(readOnly = true)
    public List<Policy> getPoliciesByUserId(Integer userId) {

        return policyRepository.findByUser_UserId(userId);
    }

    // Claimant submits a policy application.
    @Transactional
    public Policy applyForPolicy(
            String email,
            String vehicleType,
            String policyType,
            Integer durationYears) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        if (durationYears == null ||
                (durationYears != 1 && durationYears != 2)) {
            throw new IllegalArgumentException(
                    "Policy duration must be 1 or 2 years");
        }
        Policy policy = new Policy();

        try {
            policy.setVehicleType(
                    Policy.VehicleType.valueOf(
                            vehicleType.trim().toUpperCase()));

            policy.setPolicyType(
                    Policy.PolicyType.valueOf(
                            policyType.trim().toUpperCase()));

        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Invalid vehicle type or policy type");
        }

        policy.setUser(user);

        // Temporary application reference.
        policy.setPolicyNumber(
                "APP-" + UUID.randomUUID().toString().substring(0, 12)
                        .toUpperCase());

        policy.setDurationYears(durationYears);

        policy.setStartDate(LocalDate.now());
        policy.setEndDate(LocalDate.now().plusYears(durationYears));
        policy.setStatus(PolicyStatus.PENDING);

        return policyRepository.save(policy);
    }

    // Admin views pending applications.
    @Transactional(readOnly = true)
    public List<Policy> getPendingApplications() {

        return policyRepository.findByStatus(PolicyStatus.PENDING);
    }

    // Admin approves or rejects an application.
    @Transactional
    
    public Policy reviewPolicy(
            Integer policyId,
            String decision,
            String adminEmail,
            BigDecimal sumInsured,
            BigDecimal premiumAmount) {

        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("Admin not found"));

        if (admin.getRole() != User.Role.ADMIN) {
            throw new IllegalArgumentException(
                    "Only ADMIN can review policy applications");
        }

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Application not found"));

        if (policy.getStatus() != PolicyStatus.PENDING) {
            throw new IllegalArgumentException(
                    "This application has already been reviewed");
        }

        if ("APPROVED".equalsIgnoreCase(decision)) {

            if (sumInsured == null || sumInsured.compareTo(BigDecimal.ZERO) <= 0
                    || premiumAmount == null
                    || premiumAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException(
                        "Valid Sum Insured and Premium Amount are required");
            }

            policy.setSumInsured(sumInsured);
            policy.setPremiumAmount(premiumAmount);
            policy.setStatus(PolicyStatus.APPROVED);

            LocalDate startDate = LocalDate.now();

            policy.setStartDate(startDate);
            policy.setEndDate(startDate.plusYears(policy.getDurationYears()));

            policy.setPolicyNumber(
                    "MS-" + UUID.randomUUID().toString()
                            .substring(0, 12).toUpperCase());

        } else if ("REJECTED".equalsIgnoreCase(decision)) {

            policy.setStatus(PolicyStatus.REJECTED);

        } else {
            throw new IllegalArgumentException(
                    "Decision must be APPROVED or REJECTED");
        }

        return policyRepository.save(policy);
    }
    // Fetch the logged-in claimant's policies.
    @Transactional(readOnly = true)
    public List<PolicySummaryResponse> getMyPolicies(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        List<Policy> policies =
                policyRepository.findByUser_UserId(user.getUserId());

        LocalDate today = LocalDate.now();

        return policies.stream()
                .map(policy -> {

                    String status;

                    if (policy.getStatus() == PolicyStatus.PENDING) {
                        status = "PENDING";

                    } else if (policy.getStatus() == PolicyStatus.REJECTED) {
                        status = "REJECTED";

                    } else {
                        status = policy.getEndDate() != null
                                && !policy.getEndDate().isBefore(today)
                                ? "ACTIVE"
                                : "EXPIRED";
                    }

                    return new PolicySummaryResponse(
                            policy.getPolicyId(),
                            policy.getPolicyNumber(),
                            policy.getVehicleType().name(),
                            policy.getPolicyType().name(),
                            policy.getStartDate(),
                            policy.getEndDate(),
                            policy.getDurationYears(),
                            policy.getSumInsured(),
                            policy.getPremiumAmount(),
                            status
                    );
                })
                .toList();
    }
}
