package com.dataanon.api.service;

import com.dataanon.api.dto.metric.MetricDto;
import com.dataanon.api.repository.MetricDefinitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetricService {

    private final MetricDefinitionRepository metricRepository;

    public List<MetricDto> listActive() {
        return metricRepository.findByIsActiveTrue().stream()
                .map(MetricDto::from)
                .toList();
    }
}
