package com.study.service.impl;

import com.study.mapper.StudentMapper;
import com.study.pojo.PageResult;
import com.study.pojo.Student;
import com.study.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentServiceimpl implements StudentService {
    @Autowired
    StudentMapper studentMapper;
    @Override
    public PageResult<Student> PageStudent(Integer page, Integer pageSize, String name, String studentNo, Integer education, Integer classId) {
        //1.调用Mapper 查询总记录数
        Long total = studentMapper.Count(name, studentNo, education, classId);
        //2.根据分页参数查询数据
        //List<Student> list = studentMapper.PageStudent(page, pageSize, name, studentNo, education, classId);
        //3.返回分页结果
        Integer start = (page - 1) * pageSize;
        List<Student> list = studentMapper.PageStudent(start, pageSize, name, studentNo, education, classId);
        return new PageResult<>(total, list);
    }

    @Override
    public void addStudent(Student student) {
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.insertStudent(student);
    }

    @Override
    public Student getById(Integer id) {
        return studentMapper.getById(id);
    }

    @Override
    public void updateStudent(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.updateStudent(student);
    }

    @Override
    public void deleteById(Integer id) {
        studentMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void updateViolation(Integer id,Integer violation) {
        //获取学生信息
        Student student = studentMapper.getById(id);
        //检查学生是否存在（原代码判断写反，已修复为 == null）
        if(student == null){
            throw new RuntimeException("学生不存在:id="+id);
        }
        //根据id更改学生的违纪信息
        student.setUpdateTime(LocalDateTime.now());
        student.setViolationCount(student.getViolationCount()+1);
        student.setViolationScore(student.getViolationScore()+violation);
        studentMapper.updateViolation(student);
    }
}
