<template>
  <div class="login-container">
    <div class="login-card">
      <h1>🚀 学生管理系统</h1>
      <p class="subtitle">宇宙爆炸版 — Spring Boot + Vue3 + JWT + Redis + Flyway + Docker</p>

      <el-tabs v-model="activeTab" class="login-tabs">
        <!-- 登录 -->
        <el-tab-pane label="登录" name="login">
          <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef">
            <el-form-item prop="username">
              <el-input v-model="loginForm.username" placeholder="用户名" :prefix-icon="User" />
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="loginForm.password" type="password" placeholder="密码" show-password :prefix-icon="Lock" @keyup.enter="handleLogin" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="loading" @click="handleLogin" style="width:100%">登 录</el-button>
            </el-form-item>
          </el-form>
          <div class="hint">初始管理员: admin / Admin@123</div>
        </el-tab-pane>

        <!-- 注册 -->
        <el-tab-pane label="注册" name="register">
          <el-form :model="regForm" ref="regFormRef">
            <el-form-item>
              <el-input v-model="regForm.username" placeholder="用户名 (3~15位)" />
            </el-form-item>
            <el-form-item>
              <el-input v-model="regForm.password" type="password" placeholder="密码" show-password />
            </el-form-item>
            <el-form-item>
              <el-input v-model="regForm.password2" type="password" placeholder="确认密码" show-password />
            </el-form-item>
            <el-form-item>
              <el-input v-model="regForm.personId" placeholder="身份证号 (18位)" />
            </el-form-item>
            <el-form-item>
              <el-input v-model="regForm.phoneNumber" placeholder="手机号 (11位)" />
            </el-form-item>
            <el-form-item>
              <el-button type="success" :loading="loading" @click="handleRegister" style="width:100%">注 册</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 忘记密码 -->
        <el-tab-pane label="忘记密码" name="forgot">
          <el-form :model="forgotForm">
            <el-form-item>
              <el-input v-model="forgotForm.username" placeholder="用户名" />
            </el-form-item>
            <el-form-item>
              <el-input v-model="forgotForm.personId" placeholder="身份证号" />
            </el-form-item>
            <el-form-item>
              <el-input v-model="forgotForm.phone" placeholder="手机号" />
            </el-form-item>
            <el-form-item>
              <el-input v-model="forgotForm.newPassword" type="password" placeholder="新密码" show-password />
            </el-form-item>
            <el-form-item>
              <el-input v-model="forgotForm.newPassword2" type="password" placeholder="确认新密码" show-password />
            </el-form-item>
            <el-form-item>
              <el-button type="warning" :loading="loading" @click="handleResetPassword" style="width:100%">重置密码</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'

const router = useRouter()
const authStore = useAuthStore()
const activeTab = ref('login')
const loading = ref(false)

const loginForm = reactive({ username: '', password: '' })
const loginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}
const loginFormRef = ref()

const regForm = reactive({ username: '', password: '', password2: '', personId: '', phoneNumber: '' })
const regFormRef = ref()
const forgotForm = reactive({ username: '', personId: '', phone: '', newPassword: '', newPassword2: '' })

async function handleLogin() {
  const valid = await loginFormRef.value?.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    const res = await authStore.login(loginForm)
    if (res.code === 200) {
      ElMessage.success('登录成功')
      router.push('/dashboard')
    }
  } finally { loading.value = false }
}

async function handleRegister() {
  if (regForm.password !== regForm.password2) {
    ElMessage.error('两次密码不一致'); return
  }
  loading.value = true
  try {
    const res = await authStore.register(regForm)
    if (res.code === 200) {
      ElMessage.success('注册成功，请登录')
      activeTab.value = 'login'
    }
  } finally { loading.value = false }
}

async function handleResetPassword() {
  if (forgotForm.newPassword !== forgotForm.newPassword2) {
    ElMessage.error('两次密码不一致'); return
  }
  loading.value = true
  try {
    const res = await authStore.resetPassword(forgotForm)
    if (res.code === 200) {
      ElMessage.success('密码重置成功')
      activeTab.value = 'login'
    }
  } finally { loading.value = false }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-card {
  background: #fff;
  border-radius: 16px;
  padding: 40px;
  width: 440px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.15);
}
.login-card h1 { text-align: center; margin-bottom: 4px; }
.subtitle { text-align: center; color: #999; font-size: 13px; margin-bottom: 24px; }
.hint { text-align: center; color: #aaa; font-size: 12px; margin-top: 12px; }
.login-tabs :deep(.el-tabs__nav) { width: 100%; display: flex; }
.login-tabs :deep(.el-tabs__item) { flex: 1; text-align: center; }
</style>
