package com.study.service;

import com.study.pojo.Dept;

import java.util.List;

public interface DeptService {
    /*
      查询所有部分的信息
     */
    List<Dept> findAll();
    /*
    根据id删除部门
     */
    void deleteById(Integer id);
    /*
    新增部门
     */
    void add(Dept dept);

    Dept getById(Integer id);
    /*
    修改部门
     */
    void update(Dept dept);
}
