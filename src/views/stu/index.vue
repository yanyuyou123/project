<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { queryStudentList, addStudent, queryStudentById, updateStudent, deleteStudent, updateViolation } from '@/api/student'
import { queryClazzList } from '@/api/classes'

const list = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

const queryForm = ref({ name: '', studentNo: '', education: '', classId: '' })

// 学历枚举（与后端统计口径一致）
const eduMap = { 1: '初中', 2: '高中', 3: '大专', 4: '本科', 5: '硕士' }
const eduOptions = Object.entries(eduMap).map(([v, l]) => ({ value: Number(v), label: l }))

const clazzList = ref([])

const search = async () => {
  const result = await queryStudentList(
    currentPage.value, pageSize.value,
    queryForm.value.name, queryForm.value.studentNo,
    queryForm.value.education || null, queryForm.value.classId || null
  )
  if (result.code === 1) {
    list.value = result.data.rows
    total.value = result.data.total
  }
}
const clear = () => {
  queryForm.value = { name: '', studentNo: '', education: '', classId: '' }
  search()
}
const handleSizeChange = (val) => { pageSize.value = val; search() }
const handleCurrentChange = (val) => { currentPage.value = val; search() }

// 新增/编辑
const dialogVisible = ref(false)
const dialogTitle = ref('新增学员')
const isEdit = ref(false)
const emptyForm = () => ({
  id: null, name: '', studentNo: '', gender: 1, phone: '',
  education: null, classId: null
})
const form = ref(emptyForm())

const openAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增学员'
  form.value = emptyForm()
  dialogVisible.value = true
}
const openEdit = async (id) => {
  const result = await queryStudentById(id)
  if (result.code === 1) {
    form.value = { ...result.data }
    isEdit.value = true
    dialogTitle.value = '编辑学员'
    dialogVisible.value = true
  } else {
    ElMessage.error(result.msg)
  }
}

const save = async () => {
  if (!form.value.name) { ElMessage.warning('请填写学员姓名'); return }
  const result = isEdit.value ? await updateStudent(form.value) : await addStudent(form.value)
  if (result.code === 1) {
    ElMessage.success(isEdit.value ? '修改成功' : '添加成功')
    dialogVisible.value = false
    search()
  } else {
    ElMessage.error(result.msg)
  }
}

const deleteById = async (id) => {
  try { await ElMessageBox.confirm('确定删除该学员吗？', '提示', { type: 'warning' }) } catch (e) { return }
  const result = await deleteStudent(id)
  result.code === 1 ? ElMessage.success('删除成功') : ElMessage.error(result.msg)
  search()
}

// 违纪处理
const handleViolation = async (row) => {
  const score = await ElMessageBox.prompt(`学员：${row.name}，请输入本次违纪扣分（默认5分）`, '违纪处理', {
    inputValue: 5,
    inputPattern: /^[1-9]\d*$/,
    inputErrorMessage: '请输入正整数扣分'
  }).catch(() => null)
  if (score === null) return
  const result = await updateViolation(row.id, Number(score.value))
  result.code === 1 ? (ElMessage.success('违纪记录成功'), search()) : ElMessage.error(result.msg)
}

onMounted(async () => {
  search()
  // 拉取全部班级做下拉（分页参数给大值）
  const clazzResult = await queryClazzList(1, 200)
  if (clazzResult.code === 1) clazzList.value = clazzResult.data.rows
})
</script>

<template>
  <h1>学员管理</h1>

  <!-- 查询 -->
  <el-form :inline="true" :model="queryForm">
    <el-form-item label="姓名">
      <el-input v-model="queryForm.name" placeholder="请输入姓名" clearable />
    </el-form-item>
    <el-form-item label="学号">
      <el-input v-model="queryForm.studentNo" placeholder="请输入学号" clearable />
    </el-form-item>
    <el-form-item label="学历">
      <el-select v-model="queryForm.education" placeholder="请选择学历" clearable style="width: 120px">
        <el-option v-for="opt in eduOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
      </el-select>
    </el-form-item>
    <el-form-item label="班级">
      <el-select v-model="queryForm.classId" placeholder="请选择班级" clearable style="width: 140px">
        <el-option v-for="c in clazzList" :key="c.id" :label="c.className" :value="c.id" />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="search">查询</el-button>
      <el-button type="info" @click="clear">清空</el-button>
    </el-form-item>
  </el-form>

  <div class="container">
    <el-button type="primary" @click="openAdd">+ 新增学员</el-button>
  </div>

  <el-table :data="list" border>
    <el-table-column prop="name" label="姓名" width="90" align="center" />
    <el-table-column prop="studentNo" label="学号" width="120" align="center" />
    <el-table-column prop="gender" label="性别" width="70" align="center">
      <template #default="scope">{{ scope.row.gender === 1 ? '男' : '女' }}</template>
    </el-table-column>
    <el-table-column prop="phone" label="手机号" width="130" align="center" />
    <el-table-column prop="education" label="学历" width="90" align="center">
      <template #default="scope">{{ eduMap[scope.row.education] || '未知' }}</template>
    </el-table-column>
    <el-table-column prop="violationCount" label="违纪次数" width="90" align="center" />
    <el-table-column prop="violationScore" label="违纪总分" width="90" align="center" />
    <el-table-column prop="updateTime" label="更新时间" width="170" align="center" />
    <el-table-column label="操作" width="220" align="center">
      <template #default="scope">
        <el-button type="warning" size="small" @click="handleViolation(scope.row)">
          <el-icon><WarnTriangleFilled /></el-icon> 违纪
        </el-button>
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
  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="560" destroy-on-close>
    <el-form :model="form" label-width="90px">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="姓名" required>
            <el-input v-model="form.name" placeholder="请输入姓名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="学号">
            <el-input v-model="form.studentNo" placeholder="请输入学号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="性别">
            <el-select v-model="form.gender">
              <el-option label="男" :value="1" />
              <el-option label="女" :value="2" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机号">
            <el-input v-model="form.phone" placeholder="请输入手机号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="学历">
            <el-select v-model="form.education" placeholder="请选择学历" clearable>
              <el-option v-for="opt in eduOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="班级">
            <el-select v-model="form.classId" placeholder="请选择班级" clearable>
              <el-option v-for="c in clazzList" :key="c.id" :label="c.className" :value="c.id" />
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
