package com.dataanon.api.service;

import com.dataanon.api.config.CacheNames;
import com.dataanon.api.dto.metric.MetricDto;
import com.dataanon.api.repository.MetricDefinitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetricService {

    private final MetricDefinitionRepository metricRepository;

    @Cacheable(value = CacheNames.METRICS)
    public List<MetricDto> listActive() {
        return metricRepository.findByIsActiveTrue().stream()
                .map(MetricDto::from)
                .toList();
    }
}
