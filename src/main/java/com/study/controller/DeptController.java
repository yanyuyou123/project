package com.study.controller;

import com.study.pojo.Dept;
import com.study.pojo.Result;
import com.study.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
//@RequestMapping("/depts")  可以抽取公共的路径9
@RestController
public class DeptController {
    //定义日志记录器

    @Autowired
    private DeptService deptService;
    //@RequestMapping(value = "/depts",method = RequestMethod.GET)
    @GetMapping("/depts")
    public Result list(){
        //System.out.println("查询全部部门的数据");
        log.info("查询全部部门的数据");
        List<Dept> deptlist=deptService.findAll();
        return Result.success(deptlist);
    }
    /*
    删除部门
     */
    @DeleteMapping("/depts")
    public Result delete(@RequestParam("id") Integer id){
        log.info("根据ID删除部门：{}",id);
        deptService.deleteById(id);
        return Result.success();
    }
    /*
    增添部门
     */
    @PostMapping("/depts")
    public Result add(@RequestBody Dept dept){
        //System.out.println("新增部门："+dept);
        log.info("新增部门：{}",dept);
        deptService.add(dept);
        return Result.success();
    }

    /*
    根据id 查询部门
     */
    @GetMapping("/depts/{id}")
    public Result getInfo(@PathVariable("id") Integer id){
        //System.out.println("根据id查询部门:"+id);
        log.info("根据id查询部门：{}",id);
        Dept dept=deptService.getById(id);
        return Result.success(dept);
    }
    /*
    修改部门
     */
    @PutMapping("/depts")
    public Result updateInfo(@RequestBody Dept dept){
        //System.out.println("修改部门："+dept);
        log.info("修改部门：{}",dept);
        deptService.update(dept);
        return Result.success();
    }
}
