package com.study.service;

import com.study.pojo.PageResult;
import com.study.pojo.Student;

public interface StudentService {
    PageResult<Student> PageStudent(Integer page, Integer pageSize, String name, String studentNo, Integer education, Integer classId);

    void addStudent(Student student);

    Student getById(Integer id);

    void updateStudent(Student student);

    void deleteById(Integer id);

    void updateViolation(Integer id,Integer violation);
}
