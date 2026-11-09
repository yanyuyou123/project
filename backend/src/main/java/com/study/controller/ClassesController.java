package com.study.controller;

import com.study.pojo.Classes;
import com.study.pojo.Result;
import com.study.service.ClassesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/classes")
@RestController
@Slf4j
public class ClassesController {

    @Autowired
    private ClassesService classesService;
    /*
    接口描述：条件 + 分页查询班级列表，支持班级名称模糊、状态过滤
     */
    @GetMapping
    public Result pageClasses(@RequestParam(name="name",required=false) String className,
                              @RequestParam(name="status",required=false) Integer status,
                              @RequestParam(name = "page",defaultValue="1") Integer pageNum,
                              @RequestParam(name="pageSize",defaultValue="10") Integer pageSize) {
        log.info("className: {}, status: {}, pageNum: {}, pageSize: {}", className, status, pageNum, pageSize);
        return Result.success(classesService.pageClasses(className, status, pageNum, pageSize));
    }
    /*
    新增一条班级记录
     */
    @PostMapping
    public Result addClass(@RequestBody Classes classes) {
        classesService.addClass(classes);
        return Result.success();
    }
    /*
    根据Id查询班级记录
     */
    @GetMapping("/{id}")
    public Result getClassById(@PathVariable Integer id) {
        return Result.success(classesService.getById(id));
    }
    /*
    更新班级记录
     */
    @PutMapping
    public Result updateClass(@RequestBody Classes classes) {
        classesService.updateClass(classes);
        return Result.success();
    }
    /*
    删除班级记录
     */
    @DeleteMapping("/{id}")
    public Result deleteClass(@PathVariable Integer id) {
        classesService.deleteById(id);
        return Result.success();
    }
}
