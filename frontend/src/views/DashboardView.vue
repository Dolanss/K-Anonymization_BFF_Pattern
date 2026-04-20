<template>
  <div>
    <div class="page-header">
      <div>
        <h2>Dashboard</h2>
        <p class="text-muted" v-if="data">
          {{ data.sectorId ?? 'All Sectors' }} ·
          {{ data.referenceYear }}/{{ String(data.referenceMonth).padStart(2, '0') }}
        </p>
      </div>
      <select v-model="period" @change="load" class="period-select">
        <option :value="6">Last 6 months</option>
        <option :value="12">Last 12 months</option>
        <option :value="24">Last 24 months</option>
      </select>
    </div>

    <div v-if="data" class="summary-row">
      <div class="summary-chip summary-chip--up">▲ {{ data.totalRising }} Rising</div>
      <div class="summary-chip summary-chip--down">▼ {{ data.totalFalling }} Falling</div>
      <div class="summary-chip summary-chip--flat">– {{ data.totalStable }} Stable</div>
    </div>

    <div v-if="loading" class="skeleton-grid">
      <div v-for="n in 12" :key="n" class="skeleton-card" />
    </div>

    <div v-else-if="data" class="metrics-grid">
      <MetricCard
        v-for="metric in data.metrics"
        :key="metric.metricId"
        :metric="metric"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { dashboardApi } from '@/api/dashboard'
import MetricCard from '@/components/dashboard/MetricCard.vue'
import type { DashboardOverviewResponse } from '@/types'

const period = ref(12)
const loading = ref(false)
const data = ref<DashboardOverviewResponse | null>(null)

async function load() {
  loading.value = true
  try {
    const { data: res } = await dashboardApi.overview(period.value)
    data.value = res
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
}
.page-header h2 { font-size: 22px; font-weight: 700; }
.period-select {
  padding: 8px 12px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  font-size: 14px;
}
.summary-row { display: flex; gap: 10px; margin-bottom: 20px; }
.summary-chip {
  padding: 4px 14px;
  border-radius: 9999px;
  font-size: 13px;
  font-weight: 600;
}
.summary-chip--up   { background: #dcfce7; color: var(--color-success); }
.summary-chip--down { background: #fee2e2; color: var(--color-danger); }
.summary-chip--flat { background: #f1f5f9; color: var(--color-text-muted); }
.metrics-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
}
.skeleton-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
}
.skeleton-card {
  height: 160px;
  background: linear-gradient(90deg, #f1f5f9 25%, #e2e8f0 50%, #f1f5f9 75%);
  background-size: 200% 100%;
  border-radius: var(--radius);
  animation: shimmer 1.4s infinite;
}
@keyframes shimmer {
  0% { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}
</style>
