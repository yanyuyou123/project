package com.study.controller;

import com.study.pojo.Emp;
import com.study.pojo.PageResult;
import com.study.pojo.Result;
import com.study.service.EmpExprService;
import com.study.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/*
员工管理   MutiPartFile 文件上传参数
 */
@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {
    @Autowired
    private EmpService empService;
    @Autowired
    private EmpExprService empExprService;
    /*
    分页查询
     */
    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize ,
                       String name, Integer gender,
                       @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate begin,
                       @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate end) {
    //RequestParm 可以写默认参数
     log.info("分页查询：{} {} {} {} {} {}",page,pageSize,name,gender,begin,end);
     PageResult <Emp> pageresult= empService.page(page,pageSize,name,gender,begin,end);
     return Result.success(pageresult);
    }
    /*
    新增员工
     */
    @PostMapping
    public Result save(@RequestBody Emp emp) {
        empService.save(emp);
        log.info("新增员工信息：{}",emp);
        return Result.success();
    }
    /*
    删除员工
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids){
        log.info("删除员工的Id：{}", ids);
        empService.delete(ids);
        return Result.success();
    }
    /*
    根据id 查询员工基本信息
     */
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id){
        log.info("根据员工Id查询员工基本信息：{}", id);
        Emp emp=empService.getInfo(id);
        return Result.success(emp);
    }
    /*
    修改员工
     */
    @PutMapping
    public Result update(@RequestBody Emp emp){
        log.info("修改员工信息：{}",emp);
        empService.update(emp);
        return Result.success();
    }
}
