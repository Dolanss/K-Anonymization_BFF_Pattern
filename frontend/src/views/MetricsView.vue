<template>
  <div>
    <h2 style="font-size:22px;font-weight:700;margin-bottom:20px">Financial Metrics</h2>

    <div v-if="loading" class="text-muted">Loading…</div>

    <div v-else class="metrics-list card">
      <table>
        <thead>
          <tr>
            <th>#</th>
            <th>Name</th>
            <th>Description</th>
            <th>Unit</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="m in metrics" :key="m.id">
            <td class="text-muted">{{ m.id }}</td>
            <td><strong>{{ m.name }}</strong></td>
            <td class="text-muted">{{ m.description }}</td>
            <td><span class="badge badge-flat">{{ m.unit }}</span></td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import client from '@/api/client'
import type { MetricDto } from '@/types'

const metrics = ref<MetricDto[]>([])
const loading = ref(true)

onMounted(async () => {
  try {
    const { data } = await client.get<MetricDto[]>('/metrics')
    metrics.value = data
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
table { width: 100%; border-collapse: collapse; }
th, td { padding: 10px 14px; text-align: left; border-bottom: 1px solid var(--color-border); font-size: 14px; }
th { font-weight: 600; color: var(--color-text-muted); }
</style>
