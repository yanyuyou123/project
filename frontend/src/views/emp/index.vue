<script setup>
import { ref, watch, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { queryEmpList, addEmp, queryEmpById, updateEmp, deleteEmp, uploadImage } from '@/api/emp'
import { queryDeptAll } from '@/api/dept'

// ===== 查询表单 =====
const empForm = ref({ name: '', gender: '', date: [], begin: '', end: '' })
const empList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

// 岗位枚举（与后端统计 SQL 口径一致）
const jobMap = { 1: '班主任', 2: '讲师', 3: '学工主管', 4: '教研主管', 5: '咨询师' }
const jobOptions = Object.entries(jobMap).map(([v, l]) => ({ value: Number(v), label: l }))

// 部门下拉
const deptList = ref([])

const search = async () => {
  const result = await queryEmpList(
    currentPage.value, pageSize.value,
    empForm.value.name, empForm.value.gender,
    empForm.value.begin, empForm.value.end
  )
  if (result.code === 1) {
    empList.value = result.data.rows
    total.value = result.data.total
  }
}
const clear = () => {
  empForm.value = { name: '', gender: '', date: [], begin: '', end: '' }
  search()
}
// 监听日期范围，拆分 begin/end
watch(() => empForm.value.date, (val) => {
  if (val && val.length === 2) {
    empForm.value.begin = val[0]
    empForm.value.end = val[1]
  } else {
    empForm.value.begin = ''
    empForm.value.end = ''
  }
})
const handleSizeChange = (val) => { pageSize.value = val; search() }
const handleCurrentChange = (val) => { currentPage.value = val; search() }

// ===== 新增/编辑 =====
const dialogVisible = ref(false)
const dialogTitle = ref('新增员工')
const isEdit = ref(false)

const emptyForm = () => ({
  id: null, username: '', password: '', name: '', gender: 1,
  phone: '', job: null, salary: null, deptId: null, entryDate: '',
  imag: '', emprlist: []
})
const empForm2 = ref(emptyForm())

// 新增
const openAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增员工'
  empForm2.value = emptyForm()
  dialogVisible.value = true
}
// 编辑
const openEdit = async (id) => {
  const result = await queryEmpById(id)
  if (result.code === 1) {
    const e = result.data
    empForm2.value = {
      id: e.id, username: e.username, password: e.password, name: e.name,
      gender: e.gender, phone: e.phone, job: e.job, salary: e.salary,
      deptId: e.deptId, entryDate: e.entryDate, imag: e.imag,
      emprlist: e.emprlist && e.emprlist.length ? e.emprlist.map(x => ({ ...x })) : []
    }
    isEdit.value = true
    dialogTitle.value = '编辑员工'
    dialogVisible.value = true
  } else {
    ElMessage.error(result.msg)
  }
}

// ===== 头像上传 =====
const uploadAvatar = async (option) => {
  try {
    const result = await uploadImage(option.file)
    if (result.code === 1) {
      empForm2.value.imag = result.data
      ElMessage.success('头像上传成功')
    } else {
      ElMessage.error(result.msg || '上传失败')
    }
  } catch (e) {
    ElMessage.error('上传失败')
  } finally {
    option.onSuccess && option.onSuccess()
  }
}

// ===== 工作经历动态行 =====
const addExpr = () => {
  empForm2.value.emprlist.push({ begin: '', end: '', company: '', job: '' })
}
const removeExpr = (idx) => {
  empForm2.value.emprlist.splice(idx, 1)
}

// ===== 保存 =====
const save = async () => {
  if (!empForm2.value.name) { ElMessage.warning('请填写姓名'); return }
  if (!empForm2.value.username) { ElMessage.warning('请填写用户名'); return }
  const payload = { ...empForm2.value }
  // 去掉空的工作经历行
  payload.emprlist = (payload.emprlist || []).filter(x => x.company || x.job)
  let result
  if (isEdit.value) {
    result = await updateEmp(payload)
  } else {
    result = await addEmp(payload)
  }
  if (result.code === 1) {
    ElMessage.success(isEdit.value ? '修改成功' : '添加成功')
    dialogVisible.value = false
    search()
  } else {
    ElMessage.error(result.msg)
  }
}

// ===== 删除 =====
const deleteById = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该员工吗？', '提示', { type: 'warning' })
  } catch (e) { return }
  const result = await deleteEmp([id])
  result.code === 1 ? ElMessage.success('删除成功') : ElMessage.error(result.msg)
  search()
}
// 批量删除
const batchDelete = async () => {
  if (!selectedIds.value.length) { ElMessage.warning('请先勾选要删除的员工'); return }
  try {
    await ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 个员工吗？`, '提示', { type: 'warning' })
  } catch (e) { return }
  const result = await deleteEmp(selectedIds.value)
  result.code === 1 ? ElMessage.success('删除成功') : ElMessage.error(result.msg)
  search()
}

const selectedIds = ref([])

// ===== 初始化 =====
onMounted(async () => {
  search()
  const deptResult = await queryDeptAll()
  if (deptResult.code === 1) deptList.value = deptResult.data
})
</script>

<template>
  <h1>员工管理</h1>

  <!-- 查询表单 -->
  <el-form :inline="true" :model="empForm" class="demo-form-inline">
    <el-form-item label="姓名">
      <el-input v-model="empForm.name" placeholder="请输入姓名" clearable />
    </el-form-item>
    <el-form-item label="性别">
      <el-select v-model="empForm.gender" placeholder="请选择性别" clearable>
        <el-option label="男" :value="1" />
        <el-option label="女" :value="2" />
      </el-select>
    </el-form-item>
    <el-form-item label="入职时间">
      <el-date-picker
        v-model="empForm.date"
        type="daterange"
        range-separator="到"
        start-placeholder="开始时间"
        end-placeholder="结束时间"
        format="YYYY-MM-DD"
        value-format="YYYY-MM-DD"
      />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="search">查询</el-button>
      <el-button type="info" @click="clear">清空</el-button>
    </el-form-item>
  </el-form>

  <!-- 操作按钮 -->
  <div class="container">
    <el-button type="primary" @click="openAdd">+ 新增员工</el-button>
    <el-button type="danger" @click="batchDelete">- 批量删除</el-button>
  </div>

  <!-- 表格 -->
  <el-table :data="empList" border style="width: 100%" @selection-change="(v) => selectedIds = v.map(r => r.id)">
    <el-table-column type="selection" width="55" align="center" />
    <el-table-column prop="name" label="姓名" width="90" align="center" />
    <el-table-column prop="gender" label="性别" width="70" align="center">
      <template #default="scope">{{ scope.row.gender === 1 ? '男' : '女' }}</template>
    </el-table-column>
    <el-table-column prop="imag" label="头像" width="80" align="center">
      <template #default="scope">
        <el-avatar v-if="scope.row.imag" :src="scope.row.imag" :size="46" />
        <el-avatar v-else :size="46">{{ scope.row.name?.charAt(0) }}</el-avatar>
      </template>
    </el-table-column>
    <el-table-column prop="deptName" label="部门" width="100" align="center" />
    <el-table-column prop="job" label="岗位" width="110" align="center">
      <template #default="scope">{{ jobMap[scope.row.job] || '其他' }}</template>
    </el-table-column>
    <el-table-column prop="salary" label="薪资" width="100" align="center" />
    <el-table-column prop="entryDate" label="入职时间" width="120" align="center" />
    <el-table-column prop="createTime" label="创建时间" width="170" align="center" />
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

  <!-- 分页 -->
  <div class="container">
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[5, 10, 20, 30]"
      :background="true"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>

  <!-- 新增/编辑对话框 -->
  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="720" destroy-on-close>
    <el-form :model="empForm2" label-width="90px">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="用户名" required>
            <el-input v-model="empForm2.username" placeholder="请输入用户名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="姓名" required>
            <el-input v-model="empForm2.name" placeholder="请输入姓名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="密码">
            <el-input v-model="empForm2.password" type="password" placeholder="请输入密码" show-password />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="性别">
            <el-select v-model="empForm2.gender">
              <el-option label="男" :value="1" />
              <el-option label="女" :value="2" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机号">
            <el-input v-model="empForm2.phone" placeholder="请输入手机号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="岗位">
            <el-select v-model="empForm2.job" placeholder="请选择岗位" clearable>
              <el-option v-for="opt in jobOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="薪资">
            <el-input-number v-model="empForm2.salary" :min="0" :step="500" controls-position="right" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属部门">
            <el-select v-model="empForm2.deptId" placeholder="请选择部门" clearable>
              <el-option v-for="d in deptList" :key="d.id" :label="d.name" :value="d.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="入职日期">
            <el-date-picker
              v-model="empForm2.entryDate"
              type="date"
              value-format="YYYY-MM-DD"
              placeholder="请选择入职日期"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="头像">
            <el-upload :show-file-list="false" :http-request="uploadAvatar" accept="image/*">
              <el-avatar v-if="empForm2.imag" :src="empForm2.imag" :size="64" />
              <el-button v-else size="small">选择图片</el-button>
            </el-upload>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 工作经历 -->
      <el-divider>工作经历</el-divider>
      <div v-for="(item, idx) in empForm2.emprlist" :key="idx" class="expr-row">
        <el-date-picker v-model="item.begin" type="date" value-format="YYYY-MM-DD" placeholder="开始日期" />
        <el-date-picker v-model="item.end" type="date" value-format="YYYY-MM-DD" placeholder="结束日期" />
        <el-input v-model="item.company" placeholder="公司" style="width: 160px" />
        <el-input v-model="item.job" placeholder="职位" style="width: 130px" />
        <el-button type="danger" :icon="Delete" circle @click="removeExpr(idx)" />
      </div>
      <el-button type="success" size="small" @click="addExpr">+ 添加工作经历</el-button>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确认</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
.container { margin: 10px 0; }
.demo-form-inline { display: flex; flex-wrap: wrap; align-items: center; }
.demo-form-inline :deep(.el-form-item) { margin-right: 16px; }
.demo-form-inline :deep(.el-input), .demo-form-inline :deep(.el-select) { width: 150px; }
.demo-form-inline :deep(.el-date-editor) { width: 220px; }
.expr-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}
</style>
