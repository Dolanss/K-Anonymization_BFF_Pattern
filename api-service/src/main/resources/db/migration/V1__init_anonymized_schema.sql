CREATE TABLE IF NOT EXISTS anonymized.metric_definitions (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(100)  NOT NULL UNIQUE,
    description TEXT,
    unit        VARCHAR(20)   NOT NULL,
    is_active   BOOLEAN       NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS anonymized.companies (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(150) NOT NULL,
    sector_id  VARCHAR(50)  NOT NULL,
    region     VARCHAR(50)  NOT NULL,
    size       VARCHAR(20)  NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS anonymized.users (
    id            BIGSERIAL PRIMARY KEY,
    email         VARCHAR(200) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(20)  NOT NULL,
    name          VARCHAR(150) NOT NULL,
    company_id    BIGINT REFERENCES anonymized.companies (id),
    is_active     BOOLEAN      NOT NULL DEFAULT TRUE,
    last_login_at TIMESTAMP,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS anonymized.company_metric_values (
    id         BIGSERIAL PRIMARY KEY,
    company_id BIGINT         NOT NULL REFERENCES anonymized.companies (id),
    metric_id  INTEGER        NOT NULL REFERENCES anonymized.metric_definitions (id),
    year       INTEGER        NOT NULL,
    month      INTEGER        NOT NULL CHECK (month BETWEEN 1 AND 12),
    value      NUMERIC(18, 4) NOT NULL,
    CONSTRAINT uq_company_metric_period UNIQUE (company_id, metric_id, year, month)
);

CREATE TABLE IF NOT EXISTS anonymized.indicator_goals (
    id           BIGSERIAL PRIMARY KEY,
    company_id   BIGINT         NOT NULL REFERENCES anonymized.companies (id),
    metric_id    INTEGER        NOT NULL REFERENCES anonymized.metric_definitions (id),
    title        VARCHAR(200)   NOT NULL,
    notes        TEXT,
    target_value NUMERIC(18, 4) NOT NULL,
    start_year   INTEGER        NOT NULL,
    start_month  INTEGER        NOT NULL,
    end_year     INTEGER        NOT NULL,
    end_month    INTEGER        NOT NULL,
    is_active    BOOLEAN        NOT NULL DEFAULT TRUE,
    created_by   BIGINT REFERENCES anonymized.users (id),
    created_at   TIMESTAMP      NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_cmv_company_metric ON anonymized.company_metric_values (company_id, metric_id);
CREATE INDEX IF NOT EXISTS idx_goals_company ON anonymized.indicator_goals (company_id) WHERE is_active = TRUE;
