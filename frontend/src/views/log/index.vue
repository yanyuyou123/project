<script setup>
import { ref, onMounted } from 'vue'
import { queryLogs } from '@/api/log'

const logs = ref([])
const loading = ref(false)

const methodType = (m) => {
  if (m === 'GET') return 'success'
  if (m === 'POST') return 'primary'
  if (m === 'PUT' || m === 'PATCH') return 'warning'
  if (m === 'DELETE') return 'danger'
  return 'info'
}

const refresh = async () => {
  loading.value = true
  const result = await queryLogs()
  loading.value = false
  if (result.code === 1) logs.value = result.data
}

onMounted(refresh)
</script>

<template>
  <h1>操作日志</h1>
  <div class="container">
    <el-button type="primary" @click="refresh">刷新</el-button>
  </div>
  <el-table :data="logs" border v-loading="loading">
    <el-table-column type="index" label="序号" width="70" align="center" />
    <el-table-column prop="time" label="时间" width="180" align="center" />
    <el-table-column prop="user" label="操作人" width="120" align="center" />
    <el-table-column prop="method" label="方法" width="90" align="center">
      <template #default="scope">
        <el-tag :type="methodType(scope.row.method)" effect="dark">{{ scope.row.method }}</el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="path" label="请求路径" align="center" />
    <el-table-column prop="cost" label="耗时" width="110" align="center" />
    <el-table-column prop="status" label="状态码" width="90" align="center">
      <template #default="scope">
        <el-tag :type="scope.row.status === 200 ? 'success' : 'danger'">{{ scope.row.status }}</el-tag>
      </template>
    </el-table-column>
  </el-table>
  <p class="tip">说明：操作日志由后端登录拦截器自动记录（保存在内存，最多 500 条，重启后清空）。</p>
</template>

<style scoped>
.container { margin: 10px 0; }
.tip { color: #909399; font-size: 12px; margin-top: 10px; }
</style>
