package com.dataanon.anonymization.service;

import com.dataanon.anonymization.domain.anonymized.AnonymizedBenchmark;
import com.dataanon.anonymization.domain.staging.RawDataPoint;
import com.dataanon.anonymization.repository.anonymized.AnonymizedBenchmarkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class KAnonymizationService {

    private final AnonymizedBenchmarkRepository benchmarkRepository;

    @Value("${anonymization.k-threshold:5}")
    private int kThreshold;

    @Transactional("anonymizedTransactionManager")
    public AnonymizationResult anonymize(List<RawDataPoint> dataPoints) {
        Map<GroupKey, List<RawDataPoint>> groups = dataPoints.stream()
                .collect(Collectors.groupingBy(dp ->
                        new GroupKey(dp.getSectorId(), dp.getMetricId(), dp.getYear(), dp.getMonth())));

        int saved = 0;
        int skipped = 0;

        for (Map.Entry<GroupKey, List<RawDataPoint>> entry : groups.entrySet()) {
            List<RawDataPoint> group = entry.getValue();

            if (group.size() < kThreshold) {
                log.warn("Skipping group {} – only {} record(s), k threshold is {}",
                        entry.getKey(), group.size(), kThreshold);
                skipped++;
                continue;
            }

            List<Double> sorted = group.stream()
                    .map(dp -> dp.getValue().doubleValue())
                    .sorted()
                    .toList();

            GroupKey key = entry.getKey();
            AnonymizedBenchmark benchmark = benchmarkRepository
                    .findBySectorIdAndMetricIdAndYearAndMonth(key.sectorId(), key.metricId(), key.year(), key.month())
                    .orElse(AnonymizedBenchmark.builder()
                            .sectorId(key.sectorId())
                            .metricId(key.metricId())
                            .year(key.year())
                            .month(key.month())
                            .build());

            benchmark.setKCount(group.size());
            benchmark.setP25Value(percentile(sorted, 25));
            benchmark.setMedianValue(percentile(sorted, 50));
            benchmark.setP75Value(percentile(sorted, 75));
            benchmark.setUpdatedAt(LocalDateTime.now());

            benchmarkRepository.save(benchmark);
            saved++;
        }

        log.info("Anonymization complete – {} benchmarks saved, {} groups skipped (k < {})", saved, skipped, kThreshold);
        return new AnonymizationResult(saved, skipped);
    }

    private BigDecimal percentile(List<Double> sorted, int p) {
        if (sorted.isEmpty()) return BigDecimal.ZERO;
        double index = (p / 100.0) * (sorted.size() - 1);
        int lo = (int) Math.floor(index);
        int hi = (int) Math.ceil(index);
        if (lo == hi) return BigDecimal.valueOf(sorted.get(lo)).setScale(4, RoundingMode.HALF_UP);
        double frac = index - lo;
        double value = sorted.get(lo) * (1 - frac) + sorted.get(hi) * frac;
        return BigDecimal.valueOf(value).setScale(4, RoundingMode.HALF_UP);
    }

    public record AnonymizationResult(int benchmarksSaved, int groupsSkipped) {}

    private record GroupKey(String sectorId, Integer metricId, Integer year, Integer month) {}
}
