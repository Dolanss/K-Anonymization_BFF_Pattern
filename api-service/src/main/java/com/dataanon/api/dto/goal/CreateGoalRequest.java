package com.dataanon.api.dto.goal;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateGoalRequest(
        @NotBlank String title,
        String notes,
        @NotNull @Positive Integer metricId,
        @NotNull BigDecimal targetValue,
        @NotNull @Min(2000) @Max(2100) Integer startYear,
        @NotNull @Min(1) @Max(12) Integer startMonth,
        @NotNull @Min(2000) @Max(2100) Integer endYear,
        @NotNull @Min(1) @Max(12) Integer endMonth
) {}
