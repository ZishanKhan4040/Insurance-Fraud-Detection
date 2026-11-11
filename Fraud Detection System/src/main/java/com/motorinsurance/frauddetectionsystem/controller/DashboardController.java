
package com.motorinsurance.frauddetectionsystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.motorinsurance.frauddetectionsystem.dto.DashboardSummary;
import com.motorinsurance.frauddetectionsystem.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummary> getDashboardSummary(
            Authentication authentication) {

        DashboardSummary summary =
                dashboardService.getDashboardSummary(
                        authentication.getName());

        return ResponseEntity.ok(summary);
    }
}
