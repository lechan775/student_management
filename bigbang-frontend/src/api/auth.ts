import http from './request'
import type { ApiResponse, LoginRequest, LoginResponse, RegisterRequest } from '@/types'

export const authApi = {
  login(data: LoginRequest) {
    return http.post<ApiResponse<LoginResponse>>('/auth/login', data)
  },
  register(data: RegisterRequest) {
    return http.post<ApiResponse<null>>('/auth/register', data)
  },
  resetPassword(data: Record<string, string>) {
    return http.post<ApiResponse<null>>('/auth/reset-password', data)
  },
  getMe() {
    return http.get<ApiResponse<any>>('/auth/me')
  }
}
