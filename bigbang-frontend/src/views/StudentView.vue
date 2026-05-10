<template>
  <el-card>
    <template #header>
      <div style="display:flex;justify-content:space-between;align-items:center">
        <span>👨‍🎓 学生信息管理</span>
        <div>
          <el-button type="primary" @click="openAdd" v-if="authStore.role !== 'STUDENT'">➕ 添加学生</el-button>
          <el-button type="success" @click="exportExcel">📥 导出 Excel</el-button>
        </div>
      </div>
    </template>

    <!-- 搜索栏 -->
    <div style="display:flex;gap:10px;margin-bottom:16px">
      <el-input v-model="searchKeyword" placeholder="姓名关键字" clearable style="width:200px" @keyup.enter="doSearch" />
      <el-input v-model="searchDept" placeholder="院系关键字" clearable style="width:200px" @keyup.enter="doSearch" />
      <el-button @click="doSearch">🔍 搜索</el-button>
      <el-button @click="resetSearch">↻ 重置</el-button>
    </div>

    <!-- 表格 -->
    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="studentId" label="学号" width="110" />
      <el-table-column prop="name" label="姓名" width="80" />
      <el-table-column prop="age" label="年龄" width="60" />
      <el-table-column prop="sex" label="性别" width="60" />
      <el-table-column prop="department" label="院系" />
      <el-table-column prop="className" label="班级" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="phone" label="手机" width="120" />
      <el-table-column label="操作" width="150" v-if="authStore.role !== 'STUDENT'" fixed="right">
        <template #default="{ row }">
          <el-button type="warning" size="small" @click="openEdit(row)">编辑</el-button>
          <el-popconfirm title="确定删除？" @confirm="doDelete(row.id!)">
            <template #reference>
              <el-button type="danger" size="small">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      :page-sizes="[5, 10, 20, 50]"
      layout="total, sizes, prev, pager, next, jumper"
      @change="loadData"
      style="margin-top:16px;justify-content:flex-end"
    />

    <!-- 添加/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogMode === 'add' ? '添加学生' : '编辑学生'" width="520px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="学号" required>
          <el-input v-model="form.studentId" :disabled="dialogMode === 'edit'" />
        </el-form-item>
        <el-form-item label="姓名" required>
          <el-input v-model="form.name" />
        </el-form-item>
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="年龄">
              <el-input-number v-model="form.age" :min="1" :max="150" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别">
              <el-select v-model="form.sex"><el-option value="男" /><el-option value="女" /></el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="院系"><el-input v-model="form.department" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="班级"><el-input v-model="form.className" /></el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机"><el-input v-model="form.phone" /></el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveStudent" :loading="saving">{{ dialogMode === 'add' ? '添加' : '保存' }}</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { studentApi } from '@/api/student'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'
import type { Student } from '@/types'

const authStore = useAuthStore()
const loading = ref(false)
const tableData = ref<Student[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchKeyword = ref('')
const searchDept = ref('')

const dialogVisible = ref(false)
const dialogMode = ref<'add' | 'edit'>('add')
const saving = ref(false)
const editId = ref<number | null>(null)
const form = reactive<Student>({
  studentId: '', name: '', age: 18, sex: '男',
  department: '', className: '', email: '', phone: ''
})

onMounted(() => loadData())

async function loadData() {
  loading.value = true
  try {
    let res
    if (searchKeyword.value || searchDept.value) {
      res = await studentApi.search(searchKeyword.value, searchDept.value, currentPage.value - 1, pageSize.value)
    } else {
      res = await studentApi.list(currentPage.value - 1, pageSize.value)
    }
    if (res.data.code === 200) {
      tableData.value = res.data.data.content
      total.value = res.data.data.totalElements
    }
  } finally { loading.value = false }
}

function doSearch() { currentPage.value = 1; loadData() }
function resetSearch() { searchKeyword.value = ''; searchDept.value = ''; doSearch() }

function openAdd() {
  dialogMode.value = 'add'
  Object.assign(form, { studentId: '', name: '', age: 18, sex: '男', department: '', className: '', email: '', phone: '' })
  dialogVisible.value = true
}
function openEdit(row: Student) {
  dialogMode.value = 'edit'
  editId.value = row.id!
  Object.assign(form, row)
  dialogVisible.value = true
}

async function saveStudent() {
  saving.value = true
  try {
    let res
    if (dialogMode.value === 'add') {
      res = await studentApi.add(form)
    } else {
      res = await studentApi.update(editId.value!, form)
    }
    if (res.data.code === 200) {
      ElMessage.success(res.data.message)
      dialogVisible.value = false
      loadData()
    }
  } finally { saving.value = false }
}

async function doDelete(id: number) {
  const res = await studentApi.delete(id)
  if (res.data.code === 200) {
    ElMessage.success('删除成功')
    loadData()
  }
}

function exportExcel() {
  const token = localStorage.getItem('accessToken')
  window.open(`/api/export/excel?token=${token}`, '_blank')
}
</script>
