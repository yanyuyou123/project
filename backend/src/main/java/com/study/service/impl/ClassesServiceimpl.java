package com.study.service.impl;

import com.study.mapper.ClassesMapper;
import com.study.pojo.Classes;
import com.study.pojo.PageResult;
import com.study.service.ClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClassesServiceimpl implements ClassesService {
    @Autowired
    private ClassesMapper classesMapper;
    @Override
    @Transactional
    public PageResult<Classes> pageClasses(String className, Integer status, Integer pageNum, Integer pageSize) {
        //1.调用Mapper 查询总个数
        long total = classesMapper.count(className, status);
        //2.调用Mapper 查询当前页数据
        Integer start = (pageNum - 1) * pageSize;
        List<Classes> rows=classesMapper.list(className, status, start, pageSize);
        return new PageResult<Classes>(total,rows);
    }

    @Override
    public void addClass(Classes classes) {
        //.调用Mapper 新增班级记录
        classes.setCreateTime(LocalDateTime.now());
        classes.setUpdateTime(LocalDateTime.now());
        classesMapper.insertClasses(classes);
    }

    @Override
    public Classes getById(Integer id) {
        Classes classes=classesMapper.getById(id);
        return classes;
    }

    @Override
    public void updateClass(Classes classes) {
        classes.setUpdateTime(LocalDateTime.now());
        classesMapper.updateClasses(classes);
    }

    @Override
    public void deleteById(Integer id) {
        classesMapper.deleteById(id);
    }
}
