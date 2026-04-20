# Data Anonymization Platform

Privacy-first financial benchmarking SaaS using a two-layer BFF architecture with k-anonymization.

## Architecture

```
┌──────────────────────────────────────────────────────────────┐
│                        LAYER 1                               │
│              Anonymization Service  :8081                    │
│                                                              │
│  POST /api/v1/ingest  ──▶  Staging DB (raw_data_points)     │
│                                ▼                             │
│                    K-Anonymization (k ≥ 5)                   │
│                    Groups: sector × metric × period          │
│                    Computes P25 / Median / P75               │
│                                ▼                             │
│                     Anonymized DB ──────────────────────┐    │
└────────────────────────────────────────────────────────│───┘
                                                         │
┌────────────────────────────────────────────────────────▼───┐
│                        LAYER 2                              │
│                  API Service  :8080                         │
│         Reads EXCLUSIVELY from Anonymized DB                │
│         JWT Authentication · RBAC (ADMIN/ANALYST/CLIENT)   │
│         No raw data ever transits this layer                │
└─────────────────────────────────────────┬──────────────────┘
                                          │ REST / JSON
                              ┌───────────▼───────────┐
                              │    Vue 3 Frontend      │
                              │       :5173 / :3000    │
                              └───────────────────────┘
```

## Privacy Guarantee

Raw data is **never** accessible from Layer 2. The anonymized schema only contains:
- `anonymized_benchmarks` — k-anonymized statistical summaries (P25/Median/P75, k ≥ 5)
- Application tables (users, companies, goals)

## Quick Start

```bash
docker-compose up postgres -d

# Layer 1
cd anonymization-service && mvn spring-boot:run

# Layer 2
cd api-service && mvn spring-boot:run

# Frontend
cd frontend && npm install && npm run dev
```

Full stack: `docker-compose up --build`

- Frontend: http://localhost:3000
- API Swagger: http://localhost:8080/swagger-ui.html
- Anon Service Swagger: http://localhost:8081/swagger-ui.html

## Demo Credentials

| Email | Password | Role |
|---|---|---|
| admin@dataanon.local | Admin@123 | ADMIN |
| analyst@dataanon.local | Analyst@123 | ANALYST |
| client.alpha@dataanon.local | Analyst@123 | CLIENT |

## Ingest Demo Data

```bash
curl -X POST http://localhost:8081/api/v1/ingest \
  -H "Content-Type: application/json" \
  -d '{
    "dataPoints": [
      {"sourceCompanyRef":"co-001","sectorId":"MANUFACTURING","metricId":1,"year":2024,"month":1,"value":12.5},
      {"sourceCompanyRef":"co-002","sectorId":"MANUFACTURING","metricId":1,"year":2024,"month":1,"value":14.1},
      {"sourceCompanyRef":"co-003","sectorId":"MANUFACTURING","metricId":1,"year":2024,"month":1,"value":11.8},
      {"sourceCompanyRef":"co-004","sectorId":"MANUFACTURING","metricId":1,"year":2024,"month":1,"value":13.3},
      {"sourceCompanyRef":"co-005","sectorId":"MANUFACTURING","metricId":1,"year":2024,"month":1,"value":15.0}
    ]
  }'
```

## Tech Stack

| Layer | Technology |
|---|---|
| Layer 1 | Java 17 · Spring Boot 3.2 · Spring Data JPA · Flyway · PostgreSQL |
| Layer 2 | Java 17 · Spring Boot 3.2 · Spring Security 6 · JWT (JJWT 0.12) · Flyway |
| Frontend | Vue 3 · TypeScript · Vite · Pinia · Vue Router |
| Database | PostgreSQL 16 (schemas: staging, anonymized) |
