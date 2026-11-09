<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { logout } from '@/api/login'

const router = useRouter()
const username = ref(localStorage.getItem('username') || 'admin')

const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch (e) {
    return // 用户取消
  }
  try {
    await logout()
  } catch (e) {
    // 忽略退出接口异常，仍清本地登录态
  }
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-header class="header">
        <span class="title">Tlias智能学习辅助系统</span>
        <span class="right_tool">
          <span class="user">
            <el-icon><User /></el-icon>{{ username }}
          </span>
          <span class="divider">|</span>
          <a href="javascript:void(0)" @click="handleLogout">
            <el-icon><SwitchButton /></el-icon>退出登录
          </a>
        </span>
      </el-header>
      <el-container>
        <el-aside width="200px" class="aside">
          <el-menu router>
            <el-menu-item index="/index">
              <el-icon><HomeFilled /></el-icon>首页
            </el-menu-item>
            <el-sub-menu index="/manage">
              <template #title>
                <el-icon><School /></el-icon>班级学员管理
              </template>
              <el-menu-item index="/clazz"><el-icon><Grid /></el-icon>班级管理</el-menu-item>
              <el-menu-item index="/stu"><el-icon><User /></el-icon>学员管理</el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="/sys">
              <template #title>
                <el-icon><Setting /></el-icon>系统信息管理
              </template>
              <el-menu-item index="/dept"><el-icon><OfficeBuilding /></el-icon>部门管理</el-menu-item>
              <el-menu-item index="/emp"><el-icon><UserFilled /></el-icon>员工管理</el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="/Report">
              <template #title>
                <el-icon><DataAnalysis /></el-icon>数据统计管理
              </template>
              <el-menu-item index="/empReport"><el-icon><Histogram /></el-icon>员工信息统计</el-menu-item>
              <el-menu-item index="/stuReport"><el-icon><PieChart /></el-icon>学员信息统计</el-menu-item>
              <el-menu-item index="/log"><el-icon><DocumentCopy /></el-icon>日志信息统计</el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-aside>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  height: 60px;
  background: linear-gradient(90deg, #0268aa, #23c964);
  color: #fff;
  padding: 0 !important;
}
.title {
  font-size: 28px;
  font-weight: bold;
  margin-left: 24px;
}
.right_tool {
  font-size: 15px;
  margin-right: 24px;
  display: inline-flex;
  align-items: center;
  gap: 12px;
}
.right_tool a {
  color: #fff;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  cursor: pointer;
}
.right_tool .user {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}
.divider {
  opacity: 0.6;
}
/* 左侧菜单边框 */
:deep(.el-menu) {
  border-right: 1px solid #e4e7ed;
  height: 100%;
}
/* 让外层el-container占满全屏 */
:deep(.el-container) {
  height: 100vh;
}
</style>
