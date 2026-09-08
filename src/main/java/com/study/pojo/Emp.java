package com.study.pojo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
// 定义员工类
//注解：指定json序列化时的属性顺序
@JsonPropertyOrder({"id","username","password","name","gender","phone","job","salary","imag","entryDate","deptId","deptName","createTime","updateTime","emprlist"})
@Data
public class Emp {
    private  Integer id;
    private  String username;
    private  String password;
    private  String name;
    private  Integer gender;
    private String phone;
    private Integer job;
    private Integer salary;
    private String imag;
    private LocalDate entryDate;
    private Integer deptId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    //部门名称
    private String deptName;
    //工作经历
    private List<EmpExpr> emprlist;
}
