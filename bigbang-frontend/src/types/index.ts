export interface Student {
  id?: number
  studentId: string
  name: string
  age: number
  sex: string
  department: string
  className: string
  email: string
  phone: string
  avatarUrl?: string
  createdAt?: string
  updatedAt?: string
}

export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  accessToken: string
  refreshToken: string
  username: string
  role: string
}

export interface RegisterRequest {
  username: string
  password: string
  personId: string
  phoneNumber: string
}

export interface PageData<T> {
  content: T[]
  totalElements: number
  totalPages: number
  number: number
  size: number
}

export interface DashboardStats {
  totalStudents: number
  totalUsers: number
  deptDistribution: Record<string, number>
  sexDistribution: Record<string, number>
}

export interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

export interface OperationLog {
  id: number
  username: string
  operation: string
  detail: string
  ipAddress: string
  createdAt: string
}
