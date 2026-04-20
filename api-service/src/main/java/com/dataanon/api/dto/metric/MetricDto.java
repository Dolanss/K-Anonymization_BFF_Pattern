package com.dataanon.api.dto.metric;

import com.dataanon.api.domain.entity.MetricDefinition;

public record MetricDto(Integer id, String name, String description, String unit) {

    public static MetricDto from(MetricDefinition m) {
        return new MetricDto(m.getId(), m.getName(), m.getDescription(), m.getUnit().name());
    }
}
