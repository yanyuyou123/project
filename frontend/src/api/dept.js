import request from '@/utils/request'

// 部门列表查询
export const queryDeptAll =() => request.get('/depts');

//新增
export const addDept = (dept) => request.post('/depts', dept);

//根据id查询部门详情
export const queryDeptById = (id) => request.get(`/depts/${id}`);

//删除
export const deleteDept = (id) => request.delete(`/depts?id=${id}`);

//修改
export const updateDept = (dept) => request.put(`/depts`, dept);