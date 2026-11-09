import request from '@/utils/request'

// 班级分页条件查询（name 名称模糊，status 状态过滤）
export const queryClazzList = (page, pageSize, name, status) =>
  request({
    url: '/classes',
    method: 'get',
    params: { page, pageSize, name, status }
  })

// 新增班级
export const addClazz = (clazz) => request.post('/classes', clazz)

// 根据 id 查询班级详情
export const queryClazzById = (id) => request.get(`/classes/${id}`)

// 修改班级
export const updateClazz = (clazz) => request.put('/classes', clazz)

// 删除班级
export const deleteClazz = (id) => request.delete(`/classes/${id}`)
