package com.dataanon.api.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "anonymized_benchmarks", schema = "anonymized")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnonymizedBenchmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sector_id", nullable = false)
    private String sectorId;

    @Column(name = "metric_id", nullable = false)
    private Integer metricId;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer month;

    @Column(name = "k_count", nullable = false)
    private Integer kCount;

    @Column(name = "p25_value", nullable = false, precision = 18, scale = 4)
    private BigDecimal p25Value;

    @Column(name = "median_value", nullable = false, precision = 18, scale = 4)
    private BigDecimal medianValue;

    @Column(name = "p75_value", nullable = false, precision = 18, scale = 4)
    private BigDecimal p75Value;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
