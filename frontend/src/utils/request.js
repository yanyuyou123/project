import axios from 'axios'
// 创建 axios 实例
const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器：每次发请求前，从 localStorage 取 token 放进请求头
request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.token = token   // 与后端 LoginInterceptor 的 getHeader("token") 对应
    }
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截器：直接返回 body（后端 Result）；未登录(401)时清 token 并跳回登录页
request.interceptors.response.use(
  (response) => {
    return response.data
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('username')
      if (!window.location.hash.includes('/login')) {
        window.location.href = '/login'
      }
    }
    return Promise.reject(error)
  }
)

// 导出 axios 实例
export default request
