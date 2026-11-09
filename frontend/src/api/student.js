import request from '@/utils/request'

// 学员分页条件查询
export const queryStudentList = (page, pageSize, name, studentNo, education, classId) =>
  request({
    url: '/students',
    method: 'get',
    params: { page, pageSize, name, studentNo, education, classId }
  })

// 新增学员
export const addStudent = (student) => request.post('/students', student)

// 根据 id 查询学员详情
export const queryStudentById = (id) => request.get(`/students/${id}`)

// 修改学员
export const updateStudent = (student) => request.put('/students', student)

// 删除学员
export const deleteStudent = (id) => request.delete(`/students/${id}`)

// 违纪处理（id + 本次扣分）
export const updateViolation = (id, violationScore) =>
  request.patch(`/students/violation/${id}`, { violationScore })
