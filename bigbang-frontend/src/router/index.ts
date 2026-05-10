import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import AppLayout from '../components/AppLayout.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: { title: '登录' }
    },
    {
      path: '/',
      component: AppLayout,
      redirect: '/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'dashboard',
          component: () => import('../views/DashboardView.vue'),
          meta: { title: '仪表盘' }
        },
        {
          path: 'students',
          name: 'students',
          component: () => import('../views/StudentView.vue'),
          meta: { title: '学生管理' }
        },
        {
          path: 'logs',
          name: 'logs',
          component: () => import('../views/LogView.vue'),
          meta: { title: '操作日志' }
        }
      ]
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/login'
    }
  ]
})

export default router
