package com.dataanon.api.repository;

import com.dataanon.api.domain.entity.MetricDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetricDefinitionRepository extends JpaRepository<MetricDefinition, Integer> {
    List<MetricDefinition> findByIsActiveTrue();
}
