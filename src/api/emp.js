import request from '@/utils/request'

// 员工分页条件查询
export const queryEmpList = (page, pageSize, name, gender, begin, end) =>
  request({
    url: '/emps',
    method: 'get',
    params: { page, pageSize, name, gender, begin, end }
  })

// 新增员工
export const addEmp = (emp) => request.post('/emps', emp)

// 根据 id 查询员工详情（含工作经历）
export const queryEmpById = (id) => request.get(`/emps/${id}`)

// 修改员工
export const updateEmp = (emp) => request.put('/emps', emp)

// 批量删除（ids 为数组）
export const deleteEmp = (ids) => request.delete('/emps', { params: { ids } })

// 上传图片（头像）
export const uploadImage = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/upload', formData)
}
