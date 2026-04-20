package com.dataanon.api.controller;

import com.dataanon.api.domain.entity.User;
import com.dataanon.api.dto.goal.CreateGoalRequest;
import com.dataanon.api.dto.goal.GoalDto;
import com.dataanon.api.service.GoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/goals")
@RequiredArgsConstructor
@Tag(name = "Goals")
public class GoalController {

    private final GoalService goalService;

    @GetMapping
    @Operation(summary = "List active goals for a company")
    public ResponseEntity<List<GoalDto>> list(
            @PathVariable Long companyId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(goalService.listGoals(companyId, user));
    }

    @PostMapping
    @Operation(summary = "Create a new indicator goal")
    public ResponseEntity<GoalDto> create(
            @PathVariable Long companyId,
            @Valid @RequestBody CreateGoalRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(goalService.createGoal(companyId, request, user));
    }

    @DeleteMapping("/{goalId}")
    @Operation(summary = "Deactivate a goal")
    public ResponseEntity<Void> deactivate(
            @PathVariable Long companyId,
            @PathVariable Long goalId,
            @AuthenticationPrincipal User user) {
        goalService.deactivateGoal(goalId, user);
        return ResponseEntity.noContent().build();
    }
}
