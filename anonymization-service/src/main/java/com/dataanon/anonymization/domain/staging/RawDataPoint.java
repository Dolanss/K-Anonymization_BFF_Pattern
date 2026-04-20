package com.dataanon.anonymization.domain.staging;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "raw_data_points", schema = "staging")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawDataPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_id", nullable = false)
    private String batchId;

    @Column(name = "source_company_ref", nullable = false)
    private String sourceCompanyRef;

    @Column(name = "sector_id", nullable = false)
    private String sectorId;

    @Column(name = "metric_id", nullable = false)
    private Integer metricId;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal value;

    @Column(name = "processed", nullable = false)
    @Builder.Default
    private Boolean processed = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
