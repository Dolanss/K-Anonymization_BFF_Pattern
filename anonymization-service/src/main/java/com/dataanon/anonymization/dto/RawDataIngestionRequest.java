package com.dataanon.anonymization.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RawDataIngestionRequest(
        @NotEmpty @Size(max = 50000) @Valid List<DataPointDto> dataPoints
) {}
