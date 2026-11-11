
package com.motorinsurance.frauddetectionsystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.motorinsurance.frauddetectionsystem.dto.FraudEvaluationResponse;
import com.motorinsurance.frauddetectionsystem.service.FraudEvaluationService;

@RestController
@RequestMapping("/api/claims")
public class FraudEvaluationController {

    private final FraudEvaluationService fraudEvaluationService;

    public FraudEvaluationController(
            FraudEvaluationService fraudEvaluationService) {

        this.fraudEvaluationService = fraudEvaluationService;
    }

    // Fetch fraud evaluation of a specific claim.
    @GetMapping("/{claimId}/evaluation")
    public ResponseEntity<?> getFraudEvaluation(
            @PathVariable Integer claimId,
            Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication required");
        }

        try {

            FraudEvaluationResponse response =
                    fraudEvaluationService.getEvaluation(
                            claimId,
                            authentication.getName());

            return ResponseEntity.ok(response);

        } catch (AccessDeniedException ex) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ex.getMessage());

        } catch (IllegalArgumentException ex) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }
}
