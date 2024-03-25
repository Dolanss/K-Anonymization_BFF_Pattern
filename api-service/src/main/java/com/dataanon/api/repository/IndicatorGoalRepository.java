package com.dataanon.api.repository;

import com.dataanon.api.domain.entity.IndicatorGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndicatorGoalRepository extends JpaRepository<IndicatorGoal, Long> {
    List<IndicatorGoal> findByCompanyIdAndIsActiveTrue(Long companyId);
}
