
package com.motorinsurance.frauddetectionsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.motorinsurance.frauddetectionsystem.dto.ClaimRequest;
import com.motorinsurance.frauddetectionsystem.dto.ClaimSummaryResponse;
import com.motorinsurance.frauddetectionsystem.entity.FraudEvaluation;
import com.motorinsurance.frauddetectionsystem.service.ClaimService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    // Submit a new claim and evaluate fraud risk.
    @PostMapping
    public ResponseEntity<?> submitClaim(
            @Valid @RequestBody ClaimRequest request,
            Authentication authentication) {

        try {
            if (authentication == null
                    || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Authentication required");
            }

            FraudEvaluation evaluation =
                    claimService.submitClaim(
                            request, authentication.getName());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(evaluation);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(ex.getMessage());
        }
    }

    // Fetch claims belonging to the authenticated claimant.
    @GetMapping("/my")
    public ResponseEntity<?> getMyClaims(
            Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication required");
        }

        try {
            List<ClaimSummaryResponse> claims =
                    claimService.getMyClaims(authentication.getName());

            return ResponseEntity.ok(claims);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }
}
