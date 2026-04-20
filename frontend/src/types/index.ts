export interface UserInfo {
  id: number
  email: string
  name: string
  role: 'ADMIN' | 'ANALYST' | 'CLIENT'
  companyId: number | null
}

export interface MetricDto {
  id: number
  name: string
  description: string
  unit: 'INDEX' | 'CURRENCY' | 'PERCENTAGE'
}

export interface BenchmarkDataDto {
  sectorId: string
  metricId: number
  year: number
  month: number
  kCount: number
  p25Value: number
  medianValue: number
  p75Value: number
}

export interface MetricCardDto {
  metricId: number
  metricName: string
  metricUnit: string
  currentValue: number | null
  previousValue: number | null
  p25: number | null
  median: number | null
  p75: number | null
  sparkline: number[]
}

export interface DashboardOverviewResponse {
  metrics: MetricCardDto[]
  totalRising: number
  totalFalling: number
  totalStable: number
  sectorId: string | null
  referenceYear: number
  referenceMonth: number
}

export interface CompanyDto {
  id: number
  name: string
  sectorId: string
  region: string
  size: string
}

export interface GoalDto {
  id: number
  title: string
  notes: string | null
  metricId: number
  metricName: string
  targetValue: number
  startYear: number
  startMonth: number
  endYear: number
  endMonth: number
  isActive: boolean
}

export interface CreateGoalRequest {
  title: string
  notes?: string
  metricId: number
  targetValue: number
  startYear: number
  startMonth: number
  endYear: number
  endMonth: number
}
