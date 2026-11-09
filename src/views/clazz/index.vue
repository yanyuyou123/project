<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { queryClazzList, addClazz, queryClazzById, updateClazz, deleteClazz } from '@/api/classes'

const list = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

const queryForm = ref({ name: '', status: '' })

const statusMap = { 1: '未开班', 2: '已开班', 3: '已结课' }
const statusOptions = Object.entries(statusMap).map(([v, l]) => ({ value: Number(v), label: l }))

const search = async () => {
  const result = await queryClazzList(
    currentPage.value, pageSize.value,
    queryForm.value.name, queryForm.value.status
  )
  if (result.code === 1) {
    list.value = result.data.rows
    total.value = result.data.total
  }
}
const clear = () => {
  queryForm.value = { name: '', status: '' }
  search()
}
const handleSizeChange = (val) => { pageSize.value = val; search() }
const handleCurrentChange = (val) => { currentPage.value = val; search() }

// 新增/编辑
const dialogVisible = ref(false)
const dialogTitle = ref('新增班级')
const isEdit = ref(false)
const emptyForm = () => ({
  id: null, className: '', classNumber: null, mainTeacher: '',
  classRoom: '', startDate: '', endDate: '', status: 1
})
const form = ref(emptyForm())

const openAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增班级'
  form.value = emptyForm()
  dialogVisible.value = true
}
const openEdit = async (id) => {
  const result = await queryClazzById(id)
  if (result.code === 1) {
    form.value = { ...result.data }
    isEdit.value = true
    dialogTitle.value = '编辑班级'
    dialogVisible.value = true
  } else {
    ElMessage.error(result.msg)
  }
}

const save = async () => {
  if (!form.value.className) { ElMessage.warning('请填写班级名称'); return }
  const result = isEdit.value ? await updateClazz(form.value) : await addClazz(form.value)
  if (result.code === 1) {
    ElMessage.success(isEdit.value ? '修改成功' : '添加成功')
    dialogVisible.value = false
    search()
  } else {
    ElMessage.error(result.msg)
  }
}

const deleteById = async (id) => {
  try { await ElMessageBox.confirm('确定删除该班级吗？', '提示', { type: 'warning' }) } catch (e) { return }
  const result = await deleteClazz(id)
  result.code === 1 ? ElMessage.success('删除成功') : ElMessage.error(result.msg)
  search()
}

onMounted(search)
</script>

<template>
  <h1>班级管理</h1>

  <!-- 查询 -->
  <el-form :inline="true" :model="queryForm">
    <el-form-item label="班级名称">
      <el-input v-model="queryForm.name" placeholder="请输入班级名称" clearable />
    </el-form-item>
    <el-form-item label="班级状态">
      <el-select v-model="queryForm.status" placeholder="请选择状态" clearable style="width: 140px">
        <el-option v-for="opt in statusOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="search">查询</el-button>
      <el-button type="info" @click="clear">清空</el-button>
    </el-form-item>
  </el-form>

  <div class="container">
    <el-button type="primary" @click="openAdd">+ 新增班级</el-button>
  </div>

  <el-table :data="list" border>
    <el-table-column type="index" label="序号" width="70" align="center" />
    <el-table-column prop="className" label="班级名称" width="150" align="center" />
    <el-table-column prop="classNumber" label="人数" width="90" align="center" />
    <el-table-column prop="mainTeacher" label="主讲老师" width="120" align="center" />
    <el-table-column prop="classRoom" label="教室" width="110" align="center" />
    <el-table-column prop="startDate" label="开课时间" width="130" align="center" />
    <el-table-column prop="endDate" label="结课时间" width="130" align="center" />
    <el-table-column prop="status" label="状态" width="100" align="center">
      <template #default="scope">
        <el-tag :type="scope.row.status === 1 ? 'info' : (scope.row.status === 2 ? 'success' : 'warning')">
          {{ statusMap[scope.row.status] || '未知' }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="updateTime" label="更新时间" width="170" align="center" />
    <el-table-column label="操作" width="160" align="center">
      <template #default="scope">
        <el-button type="primary" size="small" @click="openEdit(scope.row.id)">
          <el-icon><EditPen /></el-icon> 编辑
        </el-button>
        <el-button type="danger" size="small" @click="deleteById(scope.row.id)">
          <el-icon><Delete /></el-icon> 删除
        </el-button>
      </template>
    </el-table-column>
  </el-table>

  <div class="container">
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[5, 10, 20]"
      :background="true"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>

  <!-- 对话框 -->
  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="640" destroy-on-close>
    <el-form :model="form" label-width="90px">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="班级名称" required>
            <el-input v-model="form.className" placeholder="请输入班级名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="班级人数">
            <el-input-number v-model="form.classNumber" :min="0" controls-position="right" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="主讲老师">
            <el-input v-model="form.mainTeacher" placeholder="请输入主讲老师" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="教室">
            <el-input v-model="form.classRoom" placeholder="请输入教室" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开课时间">
            <el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" placeholder="开课时间" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结课时间">
            <el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" placeholder="结课时间" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态">
            <el-select v-model="form.status">
              <el-option v-for="opt in statusOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确认</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
.container { margin: 10px 0; }
</style>
