import { defineStore } from 'pinia'
import { ref } from 'vue'
import { authApi } from '@/api/auth'
import type { LoginRequest, LoginResponse, RegisterRequest } from '@/types'

export const useAuthStore = defineStore('auth', () => {
  const username = ref('')
  const role = ref('')
  const loggedIn = ref(false)

  function setAuth(data: LoginResponse) {
    localStorage.setItem('accessToken', data.accessToken)
    localStorage.setItem('refreshToken', data.refreshToken)
    username.value = data.username
    role.value = data.role
    loggedIn.value = true
  }

  async function login(data: LoginRequest) {
    const res = await authApi.login(data)
    if (res.data.code === 200) {
      setAuth(res.data.data)
    }
    return res.data
  }

  async function register(data: RegisterRequest) {
    return (await authApi.register(data)).data
  }

  async function resetPassword(data: Record<string, string>) {
    return (await authApi.resetPassword(data)).data
  }

  function logout() {
    localStorage.clear()
    username.value = ''
    role.value = ''
    loggedIn.value = false
    window.location.href = '/login'
  }

  async function checkAuth() {
    const token = localStorage.getItem('accessToken')
    if (!token) return false
    try {
      const res = await authApi.getMe()
      if (res.data.code === 200) {
        username.value = res.data.data.username
        role.value = res.data.data.role
        loggedIn.value = true
        return true
      }
    } catch { /* ignore */ }
    return false
  }

  return { username, role, loggedIn, login, register, resetPassword, logout, checkAuth }
})
