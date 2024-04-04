package com.dataanon.api.controller;

import com.dataanon.api.domain.entity.User;
import com.dataanon.api.dto.dashboard.DashboardOverviewResponse;
import com.dataanon.api.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/overview")
    @Operation(summary = "Returns metric cards with values and anonymized benchmarks")
    public ResponseEntity<DashboardOverviewResponse> overview(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "12") int periodMonths) {
        return ResponseEntity.ok(dashboardService.getOverview(user, periodMonths));
    }
}
