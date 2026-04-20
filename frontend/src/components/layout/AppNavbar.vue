<template>
  <nav class="navbar">
    <div class="navbar__brand">
      <span class="navbar__logo">DataAnon</span>
      <span class="navbar__tagline">Privacy-First Benchmarking</span>
    </div>
    <div class="navbar__links">
      <RouterLink to="/dashboard" class="navbar__link">Dashboard</RouterLink>
      <RouterLink to="/benchmarks" class="navbar__link">Benchmarks</RouterLink>
      <RouterLink to="/metrics" class="navbar__link">Metrics</RouterLink>
    </div>
    <div class="navbar__user">
      <span class="text-muted">{{ auth.user?.name }}</span>
      <span class="badge badge-flat">{{ auth.user?.role }}</span>
      <button class="btn btn-ghost" @click="handleLogout">Logout</button>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { RouterLink, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
const router = useRouter()

function handleLogout() {
  auth.logout()
  router.push('/login')
}
</script>

<style scoped>
.navbar {
  display: flex;
  align-items: center;
  gap: 24px;
  padding: 0 24px;
  height: 56px;
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border);
  box-shadow: var(--shadow);
}
.navbar__brand { display: flex; flex-direction: column; line-height: 1.2; }
.navbar__logo  { font-weight: 700; font-size: 16px; color: var(--color-primary); }
.navbar__tagline { font-size: 10px; color: var(--color-text-muted); }
.navbar__links { display: flex; gap: 4px; flex: 1; }
.navbar__link  {
  padding: 6px 12px;
  border-radius: var(--radius);
  font-size: 14px;
  color: var(--color-text-muted);
  transition: all 0.15s;
}
.navbar__link:hover,
.navbar__link.router-link-active {
  background: #eff6ff;
  color: var(--color-primary);
}
.navbar__user { display: flex; align-items: center; gap: 8px; }
</style>
