package com.study.controller;

import com.study.pojo.Result;
import com.study.pojo.Student;
import com.study.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/students")
@Slf4j
public class StudentController {
    @Autowired
    StudentService studentService;
    /*
    分页条件查询学生数据
     */
    @GetMapping()
    public Result PageStudent(@RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "5") Integer pageSize,
                              String name, String studentNo, Integer education, Integer classId){
        log.info("分页条件查询学生数据:{}",name,studentNo,education,classId);
        return Result.success(studentService.PageStudent(page,pageSize,name,studentNo,education,classId));

    }
    /*
    新增学生信息
     */
    @PostMapping
    public Result AddStudent(@RequestBody Student student){
        log.info("新增学生信息:{}",student);
        studentService.addStudent(student);
        return Result.success();
    }
    /*
    根据id查询学生信息
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        log.info("根据id查询学生信息:{}",id);
        return Result.success(studentService.getById(id));
    }
    /*
    修改学生信息
     */
    @PutMapping()
    public Result updateStudent(@RequestBody Student student){
        log.info("修改学生信息:{}",student);
        studentService.updateStudent(student);
        return Result.success();
    }
    /*
    删除学生信息
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id){
        log.info("删除学生信息:{}",id);
        studentService.deleteById(id);
        return Result.success();
    }
    /*
    违纪处理
    违纪：违纪次数 + 1，增加违纪扣分
    请求路径：/students/violation/{id}
    请求方式：PATCH
     */
    @PatchMapping("/violation/{id}")
    public Result updateStudentViolation(@PathVariable Integer id,
                                         @RequestBody Map<String,Integer> vbody){
        Integer violation=vbody.get("violationScore");
        studentService.updateViolation(id,violation);
        return Result.success();
    }
}
