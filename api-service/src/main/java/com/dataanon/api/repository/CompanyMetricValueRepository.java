package com.dataanon.api.repository;

import com.dataanon.api.domain.entity.CompanyMetricValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CompanyMetricValueRepository extends JpaRepository<CompanyMetricValue, Long> {

    @Query("""
        SELECT v FROM CompanyMetricValue v
        WHERE v.company.id = :companyId
          AND v.metric.id = :metricId
        ORDER BY v.year ASC, v.month ASC
        """)
    List<CompanyMetricValue> findByCompanyAndMetricOrdered(
            @Param("companyId") Long companyId,
            @Param("metricId") Integer metricId);

    @Query("""
        SELECT v FROM CompanyMetricValue v
        WHERE v.company.id = :companyId
          AND v.metric.id IN :metricIds
          AND v.year = :year AND v.month = :month
        """)
    List<CompanyMetricValue> findLatestForMetrics(
            @Param("companyId") Long companyId,
            @Param("metricIds") List<Integer> metricIds,
            @Param("year") Integer year,
            @Param("month") Integer month);

    Optional<CompanyMetricValue> findByCompanyIdAndMetricIdAndYearAndMonth(
            Long companyId, Integer metricId, Integer year, Integer month);
}
