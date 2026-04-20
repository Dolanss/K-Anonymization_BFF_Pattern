<template>
  <div>
    <div class="page-header">
      <h2>Anonymized Benchmarks</h2>
      <p class="text-muted">Market percentiles (k ≥ 5 per group – no individual company is identifiable)</p>
    </div>

    <div class="filters card">
      <div class="filter-row">
        <div class="form-group">
          <label>Sector</label>
          <select v-model="filters.sectorId">
            <option v-for="s in sectors" :key="s" :value="s">{{ s }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>Metric</label>
          <select v-model="filters.metricId">
            <option v-for="m in metrics" :key="m.id" :value="m.id">{{ m.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>Start Year/Month</label>
          <div style="display:flex;gap:6px">
            <input v-model.number="filters.startYear" type="number" min="2020" max="2100" />
            <input v-model.number="filters.startMonth" type="number" min="1" max="12" />
          </div>
        </div>
        <div class="form-group">
          <label>End Year/Month</label>
          <div style="display:flex;gap:6px">
            <input v-model.number="filters.endYear" type="number" min="2020" max="2100" />
            <input v-model.number="filters.endMonth" type="number" min="1" max="12" />
          </div>
        </div>
        <button class="btn btn-primary" @click="load" :disabled="loading">
          {{ loading ? 'Loading…' : 'Apply' }}
        </button>
      </div>
    </div>

    <div v-if="benchmarks.length" class="benchmark-table card">
      <table>
        <thead>
          <tr>
            <th>Period</th>
            <th>P25</th>
            <th>Median</th>
            <th>P75</th>
            <th>Sample (k)</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="b in benchmarks" :key="`${b.year}-${b.month}`">
            <td>{{ b.year }}/{{ String(b.month).padStart(2, '0') }}</td>
            <td>{{ b.p25Value.toFixed(4) }}</td>
            <td class="median-cell">{{ b.medianValue.toFixed(4) }}</td>
            <td>{{ b.p75Value.toFixed(4) }}</td>
            <td>
              <span class="badge badge-flat">k={{ b.kCount }}</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-else-if="!loading" class="empty-state text-muted card">
      Select filters and click Apply to view anonymized benchmarks.
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { benchmarksApi } from '@/api/benchmarks'
import type { BenchmarkDataDto, MetricDto } from '@/types'

const sectors = ref<string[]>([])
const metrics = ref<MetricDto[]>([])
const benchmarks = ref<BenchmarkDataDto[]>([])
const loading = ref(false)

const now = new Date()
const filters = ref({
  sectorId: '',
  metricId: 1,
  startYear: now.getFullYear() - 1,
  startMonth: now.getMonth() + 1,
  endYear: now.getFullYear(),
  endMonth: now.getMonth() + 1
})

async function load() {
  if (!filters.value.sectorId) return
  loading.value = true
  try {
    const { data } = await benchmarksApi.get(
      filters.value.sectorId, filters.value.metricId,
      filters.value.startYear, filters.value.startMonth,
      filters.value.endYear, filters.value.endMonth
    )
    benchmarks.value = data
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  const [s, m] = await Promise.all([benchmarksApi.sectors(), benchmarksApi.metrics()])
  sectors.value = s.data
  metrics.value = m.data
  if (sectors.value.length) filters.value.sectorId = sectors.value[0]
})
</script>

<style scoped>
.page-header { margin-bottom: 20px; }
.page-header h2 { font-size: 22px; font-weight: 700; margin-bottom: 4px; }
.filters { margin-bottom: 16px; }
.filter-row { display: flex; gap: 16px; align-items: flex-end; flex-wrap: wrap; }
.form-group { display: flex; flex-direction: column; gap: 6px; }
.form-group label { font-size: 13px; font-weight: 500; }
.form-group select,
.form-group input {
  padding: 8px 10px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  font-size: 14px;
  min-width: 80px;
}
table { width: 100%; border-collapse: collapse; }
th, td { padding: 10px 14px; text-align: left; border-bottom: 1px solid var(--color-border); font-size: 14px; }
th { font-weight: 600; color: var(--color-text-muted); }
.median-cell { font-weight: 600; color: var(--color-primary); }
.empty-state { text-align: center; padding: 40px; }
</style>
