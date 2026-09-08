package com.study.service.impl;

import com.study.mapper.EmpExprMapper;
import com.study.mapper.EmpMapper;
import com.study.pojo.Emp;
import com.study.pojo.EmpExpr;
import com.study.pojo.PageResult;
import com.study.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class EmpServiceimpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;
    @Override
    public PageResult<Emp> page(Integer page, Integer pageSize, String name, Integer gender, LocalDate begin, LocalDate end) {
        //1.  调用Mapper接口查询数据
        Long total=empMapper.count();
        //2. 调用接口查询结果列表
        Integer start=(page-1)*pageSize;
        List<Emp>rows=empMapper.list(start,pageSize,name,gender,begin,end);
        return new PageResult<Emp>(total,rows);
    }
    @Transactional
    @Override
    public void save(Emp emp) {
        //1.保存员工基本信息
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.insert(emp);

        //2.保存员工工作经历信息
        List<EmpExpr> emprlist=emp.getEmprlist();
        if(emprlist!=null&& !emprlist.isEmpty()){
            //遍历集合，为empID复赋值
            emprlist.forEach(emprExpr->{
                emprExpr.setEmpId(emp.getId());
            });
            empExprMapper.insertBatch(emprlist);
        }
    }
    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        //1.删除员工基本信息
        empMapper.deleteByIds(ids);
        //2.删除员工工作经历信息
        empExprMapper.deleteByEmpId(ids);
    }

    @Override
    public Emp getInfo(Integer id) {
        return empMapper.getById(id);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Emp emp) {
        //1.根据id更新员工基本信息
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(emp);
        //2.根据id更新员工工作经历信息
        //2.1先删除旧的工作经历信息
        empExprMapper.deleteByEmpId(Arrays.asList(emp.getId()));
        //2.2在添加新的工作经历信息
        List<EmpExpr> exprList=emp.getEmprlist();
        if(exprList!=null&& !exprList.isEmpty()){
            exprList.forEach(expr->expr.setEmpId(emp.getId()));
            empExprMapper.insertBatch(exprList);
        }
    }
}
