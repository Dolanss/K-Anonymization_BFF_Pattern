import client from './client'
import type { BenchmarkDataDto } from '@/types'

export const benchmarksApi = {
  get: (sectorId: string, metricId: number, startYear: number, startMonth: number, endYear: number, endMonth: number) =>
    client.get<BenchmarkDataDto[]>('/benchmarks', {
      params: { sectorId, metricId, startYear, startMonth, endYear, endMonth }
    }),
  sectors: () =>
    client.get<string[]>('/benchmarks/sectors'),
  metrics: () =>
    client.get('/metrics')
}
