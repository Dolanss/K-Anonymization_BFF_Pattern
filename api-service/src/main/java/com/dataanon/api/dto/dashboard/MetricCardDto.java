package com.dataanon.api.dto.dashboard;

import java.math.BigDecimal;
import java.util.List;

public record MetricCardDto(
        Integer metricId,
        String metricName,
        String metricUnit,
        BigDecimal currentValue,
        BigDecimal previousValue,
        BigDecimal p25,
        BigDecimal median,
        BigDecimal p75,
        List<BigDecimal> sparkline
) {}
