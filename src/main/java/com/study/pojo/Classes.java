package com.study.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Classes {
    private Integer id;
    private String className;
    private Integer classNumber;
    private String mainTeacher;
    private String classRoom;
    //开课时间
    private LocalDate startDate;
    private LocalDate endDate;
    //班级状态
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
