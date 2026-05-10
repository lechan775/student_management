<template>
  <el-container style="min-height:100vh">
    <el-header class="navbar">
      <span class="brand">🚀 学生管理系统</span>
      <div class="nav-links">
        <router-link to="/dashboard" :class="{ active: $route.path === '/dashboard' }">📊 仪表盘</router-link>
        <router-link to="/students" :class="{ active: $route.path === '/students' }">👨‍🎓 学生管理</router-link>
        <router-link v-if="authStore.role === 'ADMIN'" to="/logs" :class="{ active: $route.path === '/logs' }">📋 操作日志</router-link>
      </div>
      <div class="user-area">
        <el-tag :type="roleTagType" size="small">{{ authStore.role }}</el-tag>
        <span>{{ authStore.username }}</span>
        <el-button text type="danger" @click="authStore.logout()">退出</el-button>
      </div>
    </el-header>
    <el-main class="main-content">
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const router = useRouter()

const roleTagType = computed(() => {
  switch (authStore.role) {
    case 'ADMIN': return 'danger'
    case 'TEACHER': return 'primary'
    default: return 'success'
  }
})

onMounted(async () => {
  const ok = await authStore.checkAuth()
  if (!ok) router.push('/login')
})
</script>

<style scoped>
.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  padding: 0 32px;
  height: 56px;
}
.brand { font-size: 18px; font-weight: 700; color: #667eea; }
.nav-links { display: flex; gap: 8px; }
.nav-links a {
  color: #666; text-decoration: none; padding: 6px 14px;
  border-radius: 6px; font-size: 14px; transition: all 0.2s;
}
.nav-links a.active, .nav-links a:hover { color: #667eea; background: rgba(102,126,234,0.08); }
.user-area { display: flex; align-items: center; gap: 10px; font-size: 14px; }
.main-content { background: #f0f2f5; padding: 24px 32px; }
</style>
