package com.study.mapper;

import com.study.pojo.EmpExpr;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmpExprMapper {
    /*
    插入员工工作经历
     */
    void insertBatch(List<EmpExpr> emprlist);
    /*
    根据员工id删除工作经历
     */
    void deleteByEmpId(List<Integer> empIds);
}
