package com.dataanon.api.service;

import com.dataanon.api.domain.entity.AnonymizedBenchmark;
import com.dataanon.api.domain.entity.CompanyMetricValue;
import com.dataanon.api.domain.entity.MetricDefinition;
import com.dataanon.api.domain.entity.User;
import com.dataanon.api.dto.dashboard.DashboardOverviewResponse;
import com.dataanon.api.dto.dashboard.MetricCardDto;
import com.dataanon.api.repository.AnonymizedBenchmarkRepository;
import com.dataanon.api.repository.CompanyMetricValueRepository;
import com.dataanon.api.repository.MetricDefinitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final MetricDefinitionRepository metricRepository;
    private final AnonymizedBenchmarkRepository benchmarkRepository;
    private final CompanyMetricValueRepository valueRepository;

    public DashboardOverviewResponse getOverview(User currentUser, int periodMonths) {
        LocalDate now = LocalDate.now();
        int refYear = now.getYear();
        int refMonth = now.getMonthValue() - 1 == 0 ? 12 : now.getMonthValue() - 1;
        if (now.getMonthValue() == 1) refYear--;

        Long companyId = currentUser.getCompany() != null ? currentUser.getCompany().getId() : null;
        String sectorId = currentUser.getCompany() != null ? currentUser.getCompany().getSectorId() : null;

        List<MetricDefinition> metrics = metricRepository.findByIsActiveTrue();
        List<Integer> metricIds = metrics.stream().map(MetricDefinition::getId).toList();

        List<CompanyMetricValue> currentValues = companyId != null
                ? valueRepository.findLatestForMetrics(companyId, metricIds, refYear, refMonth)
                : List.of();

        Map<Integer, BigDecimal> currentMap = currentValues.stream()
                .collect(Collectors.toMap(v -> v.getMetric().getId(), CompanyMetricValue::getValue));

        int prevMonth = refMonth - 1 == 0 ? 12 : refMonth - 1;
        int prevYear = refMonth == 1 ? refYear - 1 : refYear;
        List<CompanyMetricValue> prevValues = companyId != null
                ? valueRepository.findLatestForMetrics(companyId, metricIds, prevYear, prevMonth)
                : List.of();
        Map<Integer, BigDecimal> prevMap = prevValues.stream()
                .collect(Collectors.toMap(v -> v.getMetric().getId(), CompanyMetricValue::getValue));

        List<MetricCardDto> cards = new ArrayList<>();
        int rising = 0, falling = 0, stable = 0;

        for (MetricDefinition metric : metrics) {
            BigDecimal current = currentMap.get(metric.getId());
            BigDecimal previous = prevMap.get(metric.getId());

            Optional<AnonymizedBenchmark> benchmarkOpt = sectorId != null
                    ? benchmarkRepository.findBySectorIdAndMetricIdOrderByYearAscMonthAsc(sectorId, metric.getId())
                            .stream().filter(b -> b.getYear().equals(refYear) && b.getMonth().equals(refMonth))
                            .findFirst()
                    : Optional.empty();

            List<BigDecimal> sparkline = companyId != null
                    ? valueRepository.findByCompanyAndMetricOrdered(companyId, metric.getId())
                            .stream().map(CompanyMetricValue::getValue).toList()
                    : List.of();

            cards.add(new MetricCardDto(
                    metric.getId(),
                    metric.getName(),
                    metric.getUnit().name(),
                    current,
                    previous,
                    benchmarkOpt.map(AnonymizedBenchmark::getP25Value).orElse(null),
                    benchmarkOpt.map(AnonymizedBenchmark::getMedianValue).orElse(null),
                    benchmarkOpt.map(AnonymizedBenchmark::getP75Value).orElse(null),
                    sparkline
            ));

            if (current != null && previous != null) {
                int cmp = current.compareTo(previous);
                if (cmp > 0) rising++;
                else if (cmp < 0) falling++;
                else stable++;
            }
        }

        return new DashboardOverviewResponse(cards, rising, falling, stable, sectorId, refYear, refMonth);
    }
}
