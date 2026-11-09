package com.study.service;

import com.study.pojo.Emp;
import com.study.pojo.PageResult;

import java.time.LocalDate;
import java.util.List;

public interface EmpService {
    /*
    分页查询，展示记录数
     */
    PageResult<Emp> page(Integer page, Integer pageSize, String name, Integer gender, LocalDate begin, LocalDate end);
    /*
    保存员工信息
     */
    void save(Emp emp);
    /*
    根据id 删除员工信息
     */
    void delete(List<Integer> ids);
    /*
    根据id 查询员工信息
     */
    Emp getInfo(Integer id);
    /*
    修改员工信息
     */
    void update(Emp emp);
}
