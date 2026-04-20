package com.dataanon.api.dto.goal;

import com.dataanon.api.domain.entity.IndicatorGoal;

import java.math.BigDecimal;

public record GoalDto(
        Long id,
        String title,
        String notes,
        Integer metricId,
        String metricName,
        BigDecimal targetValue,
        Integer startYear,
        Integer startMonth,
        Integer endYear,
        Integer endMonth,
        Boolean isActive
) {
    public static GoalDto from(IndicatorGoal g) {
        return new GoalDto(
                g.getId(),
                g.getTitle(),
                g.getNotes(),
                g.getMetric().getId(),
                g.getMetric().getName(),
                g.getTargetValue(),
                g.getStartYear(),
                g.getStartMonth(),
                g.getEndYear(),
                g.getEndMonth(),
                g.getIsActive()
        );
    }
}
