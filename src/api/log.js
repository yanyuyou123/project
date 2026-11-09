import request from '@/utils/request'

// 查询操作日志
export const queryLogs = () => request.get('/logs')
