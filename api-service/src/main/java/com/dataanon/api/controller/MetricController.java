package com.dataanon.api.controller;

import com.dataanon.api.dto.metric.MetricDto;
import com.dataanon.api.service.MetricService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/metrics")
@RequiredArgsConstructor
@Tag(name = "Metrics")
public class MetricController {

    private final MetricService metricService;

    @GetMapping
    @Operation(summary = "List all active metric definitions")
    public ResponseEntity<List<MetricDto>> list() {
        return ResponseEntity.ok(metricService.listActive());
    }
}
