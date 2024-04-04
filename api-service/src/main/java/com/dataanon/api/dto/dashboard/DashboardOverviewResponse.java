package com.dataanon.api.dto.dashboard;

import java.util.List;

public record DashboardOverviewResponse(
        List<MetricCardDto> metrics,
        int totalRising,
        int totalFalling,
        int totalStable,
        String sectorId,
        Integer referenceYear,
        Integer referenceMonth
) {}
