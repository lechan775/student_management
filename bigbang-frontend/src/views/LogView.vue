<template>
  <el-card>
    <template #header><span>📋 操作日志（最近50条）</span></template>
    <el-timeline>
      <el-timeline-item
        v-for="log in logs" :key="log.id"
        :timestamp="log.createdAt"
        placement="top"
        :type="logType(log.operation)"
      >
        <strong>[{{ log.operation }}]</strong>
        {{ log.username }} — {{ log.detail }}
        <span style="color:#999;font-size:12px;margin-left:8px">IP: {{ log.ipAddress }}</span>
      </el-timeline-item>
    </el-timeline>
    <el-empty v-if="logs.length === 0" description="暂无日志" />
  </el-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { dashboardApi } from '@/api/student'
import type { OperationLog } from '@/types'

const logs = ref<OperationLog[]>([])

onMounted(async () => {
  try {
    const res = await dashboardApi.getLogs()
    if (res.data.code === 200) logs.value = res.data.data
  } catch { /* ignore */ }
})

function logType(op: string): 'primary' | 'success' | 'warning' | 'danger' {
  if (op.includes('ADD')) return 'success'
  if (op.includes('DELETE')) return 'danger'
  if (op.includes('UPDATE')) return 'warning'
  return 'primary'
}
</script>
