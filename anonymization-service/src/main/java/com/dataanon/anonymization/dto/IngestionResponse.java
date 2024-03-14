package com.dataanon.anonymization.dto;

public record IngestionResponse(
        String batchId,
        int rawPointsReceived,
        int benchmarksSaved,
        int groupsSkippedDueToK
) {}
