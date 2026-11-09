<script setup>
import { ref, onMounted } from 'vue'

const username = ref(localStorage.getItem('username') || '用户')
const today = ref('')

const modules = [
  { icon: 'OfficeBuilding', title: '部门管理', desc: '部门信息的新增、编辑、删除与查询', path: '/dept' },
  { icon: 'UserFilled', title: '员工管理', desc: '员工信息维护，支持工作经历与头像上传', path: '/emp' },
  { icon: 'Grid', title: '班级管理', desc: '班级信息维护，支持名称与状态筛选', path: '/clazz' },
  { icon: 'User', title: '学员管理', desc: '学员信息维护，支持违纪处理与扣分', path: '/stu' },
  { icon: 'Histogram', title: '数据统计', desc: '员工岗位/性别、学员学历的图表统计', path: '/empReport' },
  { icon: 'DocumentCopy', title: '操作日志', desc: '自动记录系统内的接口访问日志', path: '/log' }
]

onMounted(() => {
  const now = new Date()
  today.value = `${now.getFullYear()}年${now.getMonth() + 1}月${now.getDate()}日`
})
</script>

<template>
  <div class="home">
    <div class="welcome">
      <h1>欢迎回来，{{ username }} 👋</h1>
      <p class="date">{{ today }} · Tlias 智能学习辅助系统</p>
    </div>
    <el-row :gutter="20">
      <el-col v-for="m in modules" :key="m.path" :span="8">
        <el-card class="module-card" shadow="hover" @click="$router.push(m.path)">
          <div class="module-icon"><el-icon :size="32"><component :is="m.icon" /></el-icon></div>
          <div class="module-title">{{ m.title }}</div>
          <div class="module-desc">{{ m.desc }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.home { padding: 10px; }
.welcome {
  padding: 30px;
  border-radius: 10px;
  color: #fff;
  background: linear-gradient(120deg, #0268aa, #23c964);
  margin-bottom: 24px;
}
.welcome h1 { margin: 0 0 8px; font-size: 28px; }
.welcome .date { opacity: 0.85; margin: 0 0 10px; }
.welcome p:last-child { opacity: 0.95; margin: 0; }
.module-card { cursor: pointer; margin-bottom: 20px; }
.module-icon { color: #409EFF; margin-bottom: 8px; }
.module-title { font-size: 18px; font-weight: bold; margin-bottom: 6px; }
.module-desc { color: #909399; font-size: 13px; line-height: 1.6; }
</style>
