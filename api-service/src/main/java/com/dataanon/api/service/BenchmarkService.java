package com.dataanon.api.service;

import com.dataanon.api.dto.benchmark.BenchmarkDataDto;
import com.dataanon.api.repository.AnonymizedBenchmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BenchmarkService {

    private final AnonymizedBenchmarkRepository benchmarkRepository;

    public List<BenchmarkDataDto> getBenchmarks(
            String sectorId, Integer metricId,
            Integer startYear, Integer startMonth,
            Integer endYear, Integer endMonth) {

        return benchmarkRepository.findBySectorMetricAndPeriod(
                        sectorId, metricId, startYear, startMonth, endYear, endMonth)
                .stream()
                .map(b -> new BenchmarkDataDto(
                        b.getSectorId(),
                        b.getMetricId(),
                        b.getYear(),
                        b.getMonth(),
                        b.getKCount(),
                        b.getP25Value(),
                        b.getMedianValue(),
                        b.getP75Value()
                ))
                .toList();
    }

    public List<String> listSectors() {
        return benchmarkRepository.findDistinctSectorIds();
    }
}
