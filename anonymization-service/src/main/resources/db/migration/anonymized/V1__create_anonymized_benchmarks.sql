CREATE TABLE IF NOT EXISTS anonymized.anonymized_benchmarks (
    id              BIGSERIAL PRIMARY KEY,
    sector_id       VARCHAR(50)    NOT NULL,
    metric_id       INTEGER        NOT NULL,
    year            INTEGER        NOT NULL,
    month           INTEGER        NOT NULL CHECK (month BETWEEN 1 AND 12),
    k_count         INTEGER        NOT NULL CHECK (k_count >= 5),
    p25_value       NUMERIC(18, 4) NOT NULL,
    median_value    NUMERIC(18, 4) NOT NULL,
    p75_value       NUMERIC(18, 4) NOT NULL,
    updated_at      TIMESTAMP      NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_benchmark UNIQUE (sector_id, metric_id, year, month)
);

CREATE INDEX idx_benchmark_sector_metric ON anonymized.anonymized_benchmarks (sector_id, metric_id);
CREATE INDEX idx_benchmark_period ON anonymized.anonymized_benchmarks (year, month);
