package com.dataanon.api.domain.entity;

import com.dataanon.api.domain.enums.MetricUnit;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "metric_definitions", schema = "anonymized")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetricDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MetricUnit unit;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
