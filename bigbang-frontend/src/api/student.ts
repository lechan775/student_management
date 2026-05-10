import http from './request'
import type { ApiResponse, PageData, Student } from '@/types'

export const studentApi = {
  list(page: number, size: number) {
    return http.get<ApiResponse<PageData<Student>>>('/students', { params: { page, size } })
  },
  search(keyword: string, dept: string, page: number, size: number) {
    return http.get<ApiResponse<PageData<Student>>>('/students/search', { params: { keyword, dept, page, size } })
  },
  add(data: Partial<Student>) {
    return http.post<ApiResponse<Student>>('/students', data)
  },
  update(id: number, data: Partial<Student>) {
    return http.put<ApiResponse<Student>>(`/students/${id}`, data)
  },
  delete(id: number) {
    return http.delete<ApiResponse<null>>(`/students/${id}`)
  }
}

export const dashboardApi = {
  getStats() {
    return http.get<ApiResponse<any>>('/dashboard')
  },
  getLogs() {
    return http.get<ApiResponse<any[]>>('/logs')
  }
}
