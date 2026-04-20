import client from './client'
import type { DashboardOverviewResponse } from '@/types'

export const dashboardApi = {
  overview: (periodMonths = 12) =>
    client.get<DashboardOverviewResponse>('/dashboard/overview', { params: { periodMonths } })
}
