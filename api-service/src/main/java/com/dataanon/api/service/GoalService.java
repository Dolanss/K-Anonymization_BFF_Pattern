package com.dataanon.api.service;

import com.dataanon.api.domain.entity.Company;
import com.dataanon.api.domain.entity.IndicatorGoal;
import com.dataanon.api.domain.entity.MetricDefinition;
import com.dataanon.api.domain.entity.User;
import com.dataanon.api.domain.enums.UserRole;
import com.dataanon.api.dto.goal.CreateGoalRequest;
import com.dataanon.api.dto.goal.GoalDto;
import com.dataanon.api.repository.CompanyRepository;
import com.dataanon.api.repository.IndicatorGoalRepository;
import com.dataanon.api.repository.MetricDefinitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final IndicatorGoalRepository goalRepository;
    private final MetricDefinitionRepository metricRepository;
    private final CompanyRepository companyRepository;

    public List<GoalDto> listGoals(Long companyId, User currentUser) {
        if (currentUser.getRole() == UserRole.CLIENT) {
            Long ownCompanyId = currentUser.getCompany() != null ? currentUser.getCompany().getId() : null;
            if (!companyId.equals(ownCompanyId)) throw new AccessDeniedException("Access denied");
        }
        return goalRepository.findByCompanyIdAndIsActiveTrue(companyId).stream()
                .map(GoalDto::from)
                .toList();
    }

    public GoalDto createGoal(Long companyId, CreateGoalRequest request, User currentUser) {
        if (currentUser.getRole() == UserRole.CLIENT) {
            Long ownCompanyId = currentUser.getCompany() != null ? currentUser.getCompany().getId() : null;
            if (!companyId.equals(ownCompanyId)) throw new AccessDeniedException("Access denied");
        }

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        MetricDefinition metric = metricRepository.findById(request.metricId())
                .orElseThrow(() -> new RuntimeException("Metric not found"));

        IndicatorGoal goal = IndicatorGoal.builder()
                .company(company)
                .metric(metric)
                .title(request.title())
                .notes(request.notes())
                .targetValue(request.targetValue())
                .startYear(request.startYear())
                .startMonth(request.startMonth())
                .endYear(request.endYear())
                .endMonth(request.endMonth())
                .createdBy(currentUser)
                .build();

        return GoalDto.from(goalRepository.save(goal));
    }

    public void deactivateGoal(Long goalId, User currentUser) {
        IndicatorGoal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        if (currentUser.getRole() == UserRole.CLIENT) {
            Long ownCompanyId = currentUser.getCompany() != null ? currentUser.getCompany().getId() : null;
            if (!goal.getCompany().getId().equals(ownCompanyId)) throw new AccessDeniedException("Access denied");
        }

        goal.setIsActive(false);
        goalRepository.save(goal);
    }
}
