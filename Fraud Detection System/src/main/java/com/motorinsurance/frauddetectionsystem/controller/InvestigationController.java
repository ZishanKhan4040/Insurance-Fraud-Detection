
package com.motorinsurance.frauddetectionsystem.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.motorinsurance.frauddetectionsystem.dto.InvestigatorClaimDetails;
import com.motorinsurance.frauddetectionsystem.dto.InvestigationRequest;
import com.motorinsurance.frauddetectionsystem.entity.Investigation;
import com.motorinsurance.frauddetectionsystem.entity.User;
import com.motorinsurance.frauddetectionsystem.repository.UserRepository;
import com.motorinsurance.frauddetectionsystem.service.InvestigationService;

import jakarta.validation.Valid;
import com.motorinsurance.frauddetectionsystem.dto.InvestigatorDashboardStats;
@RestController
@RequestMapping("/api/investigations")
public class InvestigationController {

    private final InvestigationService investigationService;
    private final UserRepository userRepository;

    public InvestigationController(
            InvestigationService investigationService,
            UserRepository userRepository) {
        this.investigationService = investigationService;
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard-stats")
    public ResponseEntity<InvestigatorDashboardStats> getDashboardStats() {

        return ResponseEntity.ok(
                investigationService.getDashboardStats()
        );
    }
    
    @GetMapping("/pending-claims")
    public ResponseEntity<?> getPendingClaims() {
        return ResponseEntity.ok(
                investigationService.getPendingClaims()
        );
    }
    
    @GetMapping("/claim-details/{claimId}")
    public ResponseEntity<InvestigatorClaimDetails> getClaimDetails(
            @PathVariable Integer claimId) {
    	System.out.println("CLAIM DETAILS ENDPOINT HIT: " + claimId);
        return ResponseEntity.ok(
                investigationService.getClaimDetails(claimId)
        );
    }
    
    @PostMapping
    public ResponseEntity<?> createInvestigation(
            @Valid @RequestBody InvestigationRequest request,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User investigator = userRepository.findByEmail(principal.getName())
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        if (investigator.getRole() != User.Role.INVESTIGATOR) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only investigators can review claims");
        }

        Investigation investigation =
                investigationService.createInvestigation(
                        request, investigator.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(investigation);
    }
}
