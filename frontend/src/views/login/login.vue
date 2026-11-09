<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/login'

const router = useRouter()
const username = ref('')
const password = ref('')
const loading = ref(false)

const handleLogin = async () => {
  if (!username.value || !password.value) {
    ElMessage.warning('请输入账号和密码')
    return
  }
  loading.value = true
  try {
    const result = await login(username.value, password.value)
    if (result.code === 1) {
      // 保存 token 和用户名
      localStorage.setItem('token', result.data)
      localStorage.setItem('username', username.value)
      ElMessage.success('登录成功')
      router.push('/index')
    } else {
      ElMessage.error(result.msg)
    }
  } catch (e) {
    ElMessage.error('登录失败，请检查网络')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <el-icon class="logo-icon"><HomeFilled /></el-icon>
        <h1>Tlias智能学习辅助系统</h1>
      </div>
      <el-form class="login-form" @submit.prevent="handleLogin">
        <el-form-item>
          <el-input
            v-model="username"
            placeholder="请输入账号"
            size="large"
            clearable
            :prefix-icon="User"
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="password"
            type="password"
            placeholder="请输入密码"
            size="large"
            show-password
            :prefix-icon="Lock"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="login-btn"
            :loading="loading"
            @click="handleLogin"
          >
            登 录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #0268aa 0%, #23c964 100%);
}
.login-box {
  width: 420px;
  padding: 40px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
}
.login-header {
  text-align: center;
  margin-bottom: 36px;
}
.logo-icon {
  font-size: 48px;
  color: #0268aa;
  margin-bottom: 12px;
}
.login-header h1 {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin: 0;
}
.login-form :deep(.el-input__wrapper) {
  border-radius: 6px;
}
.login-btn {
  width: 100%;
  background: linear-gradient(90deg, #0268aa, #23c964);
  border: none;
  border-radius: 6px;
  font-size: 16px;
  letter-spacing: 4px;
}
.login-btn:hover {
  opacity: 0.9;
}
</style>
