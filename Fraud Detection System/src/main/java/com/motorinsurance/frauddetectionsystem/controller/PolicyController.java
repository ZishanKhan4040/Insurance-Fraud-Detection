
package com.motorinsurance.frauddetectionsystem.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.motorinsurance.frauddetectionsystem.dto.PolicySummaryResponse;
import com.motorinsurance.frauddetectionsystem.entity.Policy;
import com.motorinsurance.frauddetectionsystem.service.PolicyService;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    // Get policy by ID.
    @GetMapping("/{policyId}")
    public ResponseEntity<?> getPolicyById(
            @PathVariable Integer policyId) {

        try {
            return ResponseEntity.ok(
                    policyService.getPolicyById(policyId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get policy by number.
    @GetMapping("/number/{policyNumber}")
    public ResponseEntity<?> getPolicyByNumber(
            @PathVariable String policyNumber) {

        try {
            return ResponseEntity.ok(
                    policyService.getPolicyByNumber(policyNumber));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get claimant's policies.
    @GetMapping("/my")
    public ResponseEntity<?> getMyPolicies(
            Authentication authentication) {

        try {
            List<PolicySummaryResponse> policies =
                    policyService.getMyPolicies(authentication.getName());

            return ResponseEntity.ok(policies);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }

    // Claimant applies for a policy.
    @PostMapping("/apply")
    public ResponseEntity<?> applyForPolicy(
            @RequestParam String vehicleType,
            @RequestParam String policyType,
            @RequestParam Integer durationYears,
            Authentication authentication) {

        try {
            Policy policy = policyService.applyForPolicy(
                    authentication.getName(),
                    vehicleType,
                    policyType,
                    durationYears);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(policy);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(ex.getMessage());
        }
    }

    // Admin views pending applications.
    @GetMapping("/admin/pending")
    public ResponseEntity<?> getPendingApplications() {

        return ResponseEntity.ok(
                policyService.getPendingApplications());
    }

    // Admin approves or rejects an application.
    @PostMapping("/admin/{policyId}/decision")
    public ResponseEntity<?> reviewPolicy(
            @PathVariable Integer policyId,
            @RequestParam String decision,
            @RequestParam(required = false) BigDecimal sumInsured,
            @RequestParam(required = false) BigDecimal premiumAmount,
            Authentication authentication) {

        try {
            Policy policy = policyService.reviewPolicy(
                    policyId,
                    decision,
                    authentication.getName(),
                    sumInsured,
                    premiumAmount);

            return ResponseEntity.ok(policy);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(ex.getMessage());
        }
    }
}
