import request from '@/utils/request'

// 登录
export const login = (username, password) => request.post('/login', { username, password })

// 退出登录
export const logout = () => request.post('/logout')
