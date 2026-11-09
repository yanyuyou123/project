import request from '@/utils/request'

// 员工岗位人数统计
export const getEmpJobData = () => request.get('/report/empJobData')

// 员工性别人数统计
export const getEmpGenderData = () => request.get('/report/empGenderData')

// 学员学历分布统计
export const getStudentsEducation = () => request.get('/report/studentsEducation')
