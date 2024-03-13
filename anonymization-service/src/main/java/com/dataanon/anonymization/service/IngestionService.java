package com.dataanon.anonymization.service;

import com.dataanon.anonymization.domain.staging.RawDataPoint;
import com.dataanon.anonymization.dto.DataPointDto;
import com.dataanon.anonymization.dto.IngestionResponse;
import com.dataanon.anonymization.dto.RawDataIngestionRequest;
import com.dataanon.anonymization.repository.staging.RawDataPointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class IngestionService {

    private final RawDataPointRepository rawDataPointRepository;
    private final KAnonymizationService kAnonymizationService;

    @Transactional("stagingTransactionManager")
    public IngestionResponse ingest(RawDataIngestionRequest request) {
        String batchId = UUID.randomUUID().toString();
        log.info("Starting ingestion batch {} with {} data points", batchId, request.dataPoints().size());

        List<RawDataPoint> entities = request.dataPoints().stream()
                .map(dto -> toEntity(dto, batchId))
                .toList();

        rawDataPointRepository.saveAll(entities);
        log.info("Persisted {} raw data points in staging for batch {}", entities.size(), batchId);

        KAnonymizationService.AnonymizationResult result = kAnonymizationService.anonymize(entities);

        entities.forEach(e -> e.setProcessed(true));
        rawDataPointRepository.saveAll(entities);

        return new IngestionResponse(
                batchId,
                entities.size(),
                result.benchmarksSaved(),
                result.groupsSkipped()
        );
    }

    private RawDataPoint toEntity(DataPointDto dto, String batchId) {
        return RawDataPoint.builder()
                .batchId(batchId)
                .sourceCompanyRef(dto.sourceCompanyRef())
                .sectorId(dto.sectorId())
                .metricId(dto.metricId())
                .year(dto.year())
                .month(dto.month())
                .value(dto.value())
                .build();
    }
}
