<template>
  <div class="metric-card card">
    <div class="metric-card__header">
      <span class="metric-card__name">{{ metric.metricName }}</span>
      <span
        class="badge"
        :class="{
          'badge-up':   trend > 0,
          'badge-down': trend < 0,
          'badge-flat': trend === 0
        }"
      >
        {{ trend > 0 ? '▲' : trend < 0 ? '▼' : '–' }}
        {{ trendLabel }}
      </span>
    </div>

    <div class="metric-card__value">
      {{ formatValue(metric.currentValue, metric.metricUnit) }}
    </div>

    <div v-if="metric.sparkline.length" class="metric-card__sparkline">
      <svg viewBox="0 0 120 32" preserveAspectRatio="none">
        <polyline
          :points="sparklinePoints"
          fill="none"
          :stroke="trend >= 0 ? '#16a34a' : '#dc2626'"
          stroke-width="1.5"
          stroke-linecap="round"
          stroke-linejoin="round"
        />
      </svg>
    </div>

    <div v-if="metric.median != null" class="metric-card__benchmarks">
      <div class="benchmark-bar">
        <div
          class="benchmark-bar__fill"
          :style="{ width: percentilePosition + '%' }"
        />
        <div
          class="benchmark-bar__marker"
          :style="{ left: percentilePosition + '%' }"
          title="Your position"
        />
      </div>
      <div class="benchmark-labels text-muted">
        <span>P25: {{ formatValue(metric.p25, metric.metricUnit) }}</span>
        <span>Med: {{ formatValue(metric.median, metric.metricUnit) }}</span>
        <span>P75: {{ formatValue(metric.p75, metric.metricUnit) }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { MetricCardDto } from '@/types'

const props = defineProps<{ metric: MetricCardDto }>()

const trend = computed(() => {
  if (props.metric.currentValue == null || props.metric.previousValue == null) return 0
  return props.metric.currentValue - props.metric.previousValue
})

const trendLabel = computed(() => {
  if (props.metric.previousValue == null || props.metric.previousValue === 0) return ''
  const pct = ((trend.value / Math.abs(props.metric.previousValue)) * 100).toFixed(1)
  return `${pct}%`
})

const sparklinePoints = computed(() => {
  const s = props.metric.sparkline
  if (!s.length) return ''
  const min = Math.min(...s)
  const max = Math.max(...s)
  const range = max - min || 1
  return s.map((v, i) => {
    const x = (i / (s.length - 1)) * 120
    const y = 32 - ((v - min) / range) * 28
    return `${x},${y}`
  }).join(' ')
})

const percentilePosition = computed(() => {
  const { currentValue, p25, p75 } = props.metric
  if (currentValue == null || p25 == null || p75 == null) return 50
  const range = p75 - p25 || 1
  return Math.min(100, Math.max(0, ((currentValue - p25) / range) * 100))
})

function formatValue(val: number | null, unit: string): string {
  if (val == null) return '–'
  if (unit === 'CURRENCY') return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD', notation: 'compact' }).format(val)
  if (unit === 'PERCENTAGE') return `${val.toFixed(1)}%`
  return val.toFixed(2)
}
</script>

<style scoped>
.metric-card { display: flex; flex-direction: column; gap: 12px; }
.metric-card__header { display: flex; justify-content: space-between; align-items: flex-start; }
.metric-card__name { font-size: 13px; font-weight: 500; color: var(--color-text-muted); }
.metric-card__value { font-size: 28px; font-weight: 700; color: var(--color-text); }
.metric-card__sparkline { height: 32px; }
.metric-card__sparkline svg { width: 100%; height: 100%; }
.benchmark-bar {
  position: relative;
  height: 6px;
  background: var(--color-border);
  border-radius: 3px;
  overflow: visible;
}
.benchmark-bar__fill {
  height: 100%;
  background: var(--color-primary);
  border-radius: 3px;
  transition: width 0.3s;
}
.benchmark-bar__marker {
  position: absolute;
  top: -3px;
  width: 12px;
  height: 12px;
  background: var(--color-primary-dark);
  border: 2px solid #fff;
  border-radius: 50%;
  transform: translateX(-50%);
}
.benchmark-labels { display: flex; justify-content: space-between; font-size: 11px; }
</style>
