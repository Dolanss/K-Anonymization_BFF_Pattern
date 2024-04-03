package com.dataanon.api.controller;

import com.dataanon.api.dto.benchmark.BenchmarkDataDto;
import com.dataanon.api.service.BenchmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/benchmarks")
@RequiredArgsConstructor
@Tag(name = "Benchmarks")
public class BenchmarkController {

    private final BenchmarkService benchmarkService;

    @GetMapping
    @Operation(summary = "Get anonymized benchmark percentiles for a sector and metric over a period")
    public ResponseEntity<List<BenchmarkDataDto>> getBenchmarks(
            @RequestParam String sectorId,
            @RequestParam Integer metricId,
            @RequestParam Integer startYear,
            @RequestParam Integer startMonth,
            @RequestParam Integer endYear,
            @RequestParam Integer endMonth) {
        return ResponseEntity.ok(
                benchmarkService.getBenchmarks(sectorId, metricId, startYear, startMonth, endYear, endMonth));
    }

    @GetMapping("/sectors")
    @Operation(summary = "List available sector IDs that have anonymized benchmarks")
    public ResponseEntity<List<String>> sectors() {
        return ResponseEntity.ok(benchmarkService.listSectors());
    }
}
