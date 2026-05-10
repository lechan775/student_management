<template>
  <div>
    <el-row :gutter="16" style="margin-bottom:20px">
      <el-col :span="6"><el-card><div class="stat-num">{{ stats.totalStudents }}</div><div class="stat-label">学生总数</div></el-card></el-col>
      <el-col :span="6"><el-card><div class="stat-num">{{ stats.totalUsers }}</div><div class="stat-label">用户总数</div></el-card></el-col>
      <el-col :span="6"><el-card><div class="stat-num">{{ deptCount }}</div><div class="stat-label">院系数</div></el-card></el-col>
      <el-col :span="6"><el-card><div class="stat-num">{{ sexRatio }}</div><div class="stat-label">男女比</div></el-card></el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :span="12">
        <el-card><div ref="deptChart" style="height:360px"></div></el-card>
      </el-col>
      <el-col :span="12">
        <el-card><div ref="sexChart" style="height:360px"></div></el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick } from 'vue'
import { dashboardApi } from '@/api/student'
import * as echarts from 'echarts'
import type { DashboardStats } from '@/types'

const stats = ref<DashboardStats>({ totalStudents: 0, totalUsers: 0, deptDistribution: {}, sexDistribution: {} })
const deptChart = ref<HTMLDivElement>()
const sexChart = ref<HTMLDivElement>()

const deptCount = computed(() => Object.keys(stats.value.deptDistribution).length)
const sexRatio = computed(() => {
  const m = stats.value.sexDistribution['男'] || 0
  const f = stats.value.sexDistribution['女'] || 0
  return f === 0 ? '全男' : (m / f).toFixed(1) + ':1'
})

onMounted(async () => {
  try {
    const res = await dashboardApi.getStats()
    if (res.data.code === 200) stats.value = res.data.data
  } catch { /* ignore */ }
  await nextTick()
  renderCharts()
})

function renderCharts() {
  if (deptChart.value) {
    const chart = echarts.init(deptChart.value)
    chart.setOption({
      title: { text: '院系分布', left: 'center', textStyle: { fontSize: 14 } },
      tooltip: { trigger: 'item' },
      legend: { bottom: 0 },
      series: [{
        type: 'pie', radius: ['40%', '70%'],
        data: Object.entries(stats.value.deptDistribution).map(([k, v]) => ({ name: k, value: v }))
      }]
    })
  }
  if (sexChart.value) {
    const chart = echarts.init(sexChart.value)
    chart.setOption({
      title: { text: '性别分布', left: 'center', textStyle: { fontSize: 14 } },
      tooltip: { trigger: 'item' },
      series: [{
        type: 'pie', radius: '65%',
        data: Object.entries(stats.value.sexDistribution).map(([k, v]) => ({ name: k, value: v })),
        label: { formatter: '{b}: {c}人\n({d}%)' }
      }]
    })
  }
}
</script>

<style scoped>
.stat-num { font-size: 32px; font-weight: 700; color: #667eea; text-align: center; }
.stat-label { color: #999; font-size: 14px; text-align: center; margin-top: 4px; }
</style>
