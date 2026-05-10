import axios from 'axios'
import type { AxiosInstance, AxiosResponse } from 'axios'
import type { ApiResponse, LoginResponse } from '@/types'
import { ElMessage } from 'element-plus'

const http: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' }
})

// 请求拦截器：自动附加 accessToken
http.interceptors.request.use(config => {
  const token = localStorage.getItem('accessToken')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截器：401 自动刷新 token
let isRefreshing = false
let refreshSubscribers: ((token: string) => void)[] = []

function subscribeTokenRefresh(cb: (token: string) => void) {
  refreshSubscribers.push(cb)
}
function onTokenRefreshed(token: string) {
  refreshSubscribers.forEach(cb => cb(token))
  refreshSubscribers = []
}

http.interceptors.response.use(
  (response: AxiosResponse<ApiResponse<any>>) => response,
  async error => {
    const originalRequest = error.config

    if (error.response?.status === 401 && !originalRequest._retry) {
      const refreshToken = localStorage.getItem('refreshToken')
      if (!refreshToken) {
        localStorage.clear()
        window.location.href = '/login'
        return Promise.reject(error)
      }

      if (isRefreshing) {
        return new Promise(resolve => {
          subscribeTokenRefresh((token: string) => {
            originalRequest.headers.Authorization = `Bearer ${token}`
            resolve(http(originalRequest))
          })
        })
      }

      originalRequest._retry = true
      isRefreshing = true

      try {
        const { data } = await axios.post<ApiResponse<LoginResponse>>('/api/auth/refresh', { refreshToken })
        if (data.code === 200) {
          const { accessToken, refreshToken: newRefresh } = data.data
          localStorage.setItem('accessToken', accessToken)
          localStorage.setItem('refreshToken', newRefresh)
          onTokenRefreshed(accessToken)
          originalRequest.headers.Authorization = `Bearer ${accessToken}`
          return http(originalRequest)
        }
      } catch {
        localStorage.clear()
        window.location.href = '/login'
      } finally {
        isRefreshing = false
      }
    }

    ElMessage.error(error.response?.data?.message || '请求失败')
    return Promise.reject(error)
  }
)

export default http
