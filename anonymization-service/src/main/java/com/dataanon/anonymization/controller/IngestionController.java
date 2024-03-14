package com.dataanon.anonymization.controller;

import com.dataanon.anonymization.dto.IngestionResponse;
import com.dataanon.anonymization.dto.RawDataIngestionRequest;
import com.dataanon.anonymization.service.IngestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ingest")
@RequiredArgsConstructor
@Tag(name = "Ingestion", description = "Receives raw financial data and triggers k-anonymization")
public class IngestionController {

    private final IngestionService ingestionService;

    @PostMapping
    @Operation(
        summary = "Ingest raw data batch",
        description = "Accepts a batch of raw company financial data points. Groups by (sector, metric, year, month) and applies k-anonymization (k≥5). Raw data never leaves this service."
    )
    public ResponseEntity<IngestionResponse> ingest(@Valid @RequestBody RawDataIngestionRequest request) {
        IngestionResponse response = ingestionService.ingest(request);
        return ResponseEntity.accepted().body(response);
    }
}
