<template>
  <div class="login-page">
    <div class="login-card card">
      <div class="login-card__header">
        <h1>DataAnon</h1>
        <p class="text-muted">Privacy-First Financial Benchmarking</p>
      </div>

      <form class="login-form" @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="email">Email</label>
          <input
            id="email"
            v-model="form.email"
            type="email"
            placeholder="user@dataanon.local"
            required
            :disabled="loading"
          />
        </div>

        <div class="form-group">
          <label for="password">Password</label>
          <input
            id="password"
            v-model="form.password"
            type="password"
            placeholder="••••••••"
            required
            :disabled="loading"
          />
        </div>

        <p v-if="error" class="login-error">{{ error }}</p>

        <button type="submit" class="btn btn-primary login-btn" :disabled="loading">
          {{ loading ? 'Signing in…' : 'Sign in' }}
        </button>
      </form>

      <p class="login-hint text-muted">
        Demo: admin@dataanon.local / Admin@123
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
const router = useRouter()

const form = reactive({ email: '', password: '' })
const loading = ref(false)
const error = ref('')

async function handleLogin() {
  loading.value = true
  error.value = ''
  try {
    await auth.login(form.email, form.password)
    router.push('/dashboard')
  } catch (e: any) {
    error.value = e.response?.data?.message ?? 'Authentication failed'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-bg);
}
.login-card {
  width: 100%;
  max-width: 400px;
  padding: 40px;
}
.login-card__header { text-align: center; margin-bottom: 32px; }
.login-card__header h1 { font-size: 28px; font-weight: 700; color: var(--color-primary); }
.form-group { display: flex; flex-direction: column; gap: 6px; margin-bottom: 16px; }
.form-group label { font-size: 14px; font-weight: 500; }
.form-group input {
  padding: 10px 12px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  font-size: 14px;
  outline: none;
  transition: border-color 0.15s;
}
.form-group input:focus { border-color: var(--color-primary); }
.login-error { color: var(--color-danger); font-size: 13px; margin-bottom: 12px; }
.login-btn { width: 100%; justify-content: center; padding: 12px; }
.login-hint { text-align: center; margin-top: 20px; font-size: 12px; }
</style>
