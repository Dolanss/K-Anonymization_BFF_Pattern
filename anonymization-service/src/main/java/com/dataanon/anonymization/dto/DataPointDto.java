package com.dataanon.anonymization.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record DataPointDto(
        @NotBlank String sourceCompanyRef,
        @NotBlank String sectorId,
        @NotNull @Positive Integer metricId,
        @NotNull @Min(2000) @Max(2100) Integer year,
        @NotNull @Min(1) @Max(12) Integer month,
        @NotNull BigDecimal value
) {}
