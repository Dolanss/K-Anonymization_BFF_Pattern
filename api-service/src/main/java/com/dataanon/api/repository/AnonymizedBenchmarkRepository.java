package com.dataanon.api.repository;

import com.dataanon.api.domain.entity.AnonymizedBenchmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnonymizedBenchmarkRepository extends JpaRepository<AnonymizedBenchmark, Long> {

    List<AnonymizedBenchmark> findBySectorIdAndMetricIdOrderByYearAscMonthAsc(
            String sectorId, Integer metricId);

    @Query("""
        SELECT b FROM AnonymizedBenchmark b
        WHERE b.sectorId = :sectorId
          AND b.metricId = :metricId
          AND (b.year > :startYear OR (b.year = :startYear AND b.month >= :startMonth))
          AND (b.year < :endYear   OR (b.year = :endYear   AND b.month <= :endMonth))
        ORDER BY b.year ASC, b.month ASC
        """)
    List<AnonymizedBenchmark> findBySectorMetricAndPeriod(
            @Param("sectorId") String sectorId,
            @Param("metricId") Integer metricId,
            @Param("startYear") Integer startYear,
            @Param("startMonth") Integer startMonth,
            @Param("endYear") Integer endYear,
            @Param("endMonth") Integer endMonth);

    @Query("SELECT DISTINCT b.sectorId FROM AnonymizedBenchmark b")
    List<String> findDistinctSectorIds();
}
