package com.dataanon.anonymization.repository.anonymized;

import com.dataanon.anonymization.domain.anonymized.AnonymizedBenchmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnonymizedBenchmarkRepository extends JpaRepository<AnonymizedBenchmark, Long> {

    Optional<AnonymizedBenchmark> findBySectorIdAndMetricIdAndYearAndMonth(
            String sectorId, Integer metricId, Integer year, Integer month);
}
