package com.study.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private Integer id;
    private String name;
    private String studentNo;
    private Integer gender;
    private String phone;
    private Integer education;
    private Integer classId;
    private Integer violationCount;
    private Integer violationScore;
    private LocalDateTime CreateTime;
    private LocalDateTime UpdateTime;
}
