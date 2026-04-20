CREATE TABLE IF NOT EXISTS staging.raw_data_points (
    id              BIGSERIAL PRIMARY KEY,
    batch_id        VARCHAR(36)     NOT NULL,
    source_company_ref VARCHAR(100) NOT NULL,
    sector_id       VARCHAR(50)     NOT NULL,
    metric_id       INTEGER         NOT NULL,
    year            INTEGER         NOT NULL,
    month           INTEGER         NOT NULL CHECK (month BETWEEN 1 AND 12),
    value           NUMERIC(18, 4)  NOT NULL,
    processed       BOOLEAN         NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_raw_data_batch ON staging.raw_data_points (batch_id);
CREATE INDEX idx_raw_data_unprocessed ON staging.raw_data_points (processed) WHERE processed = FALSE;
