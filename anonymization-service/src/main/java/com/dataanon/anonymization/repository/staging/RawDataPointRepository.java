package com.dataanon.anonymization.repository.staging;

import com.dataanon.anonymization.domain.staging.RawDataPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RawDataPointRepository extends JpaRepository<RawDataPoint, Long> {

    @Query("SELECT r FROM RawDataPoint r WHERE r.batchId = :batchId AND r.processed = false")
    List<RawDataPoint> findUnprocessedByBatch(@Param("batchId") String batchId);

    List<RawDataPoint> findByProcessedFalse();
}
