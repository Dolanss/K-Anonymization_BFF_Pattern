package com.dataanon.api.dto.benchmark;

import java.math.BigDecimal;

public record BenchmarkDataDto(
        String sectorId,
        Integer metricId,
        Integer year,
        Integer month,
        Integer kCount,
        BigDecimal p25Value,
        BigDecimal medianValue,
        BigDecimal p75Value
) {}
