package com.study.controller;

import com.study.pojo.JobOption;
import com.study.pojo.Result;
import com.study.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
@Slf4j
public class ReportController {
    @Autowired
    private ReportService reportService;
    @GetMapping("/empJobData")
    public Result getEmpJobData(){
        log.info("统计员工人数");
       JobOption jobOption =reportService.getEmpJobData();
       return Result.success(jobOption);
    }
    @GetMapping("/empGenderData")
    public Result getEmpGenderData(){
        log.info("统计员工性别");
        return Result.success(reportService.getEmpGenderData());
    }
    /*
    学员学历统计
     */
    @GetMapping("/studentsEducation")
    public Result getStudentsEducation(){
        log.info("统计学员学历");
        return Result.success(reportService.getStudentsEducation());
    }
}

