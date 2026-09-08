package com.study.service;

import com.study.pojo.JobOption;

import java.util.List;
import java.util.Map;

public interface ReportService {
    /*
    统计员工人数信息
     */
    JobOption getEmpJobData();


    List<Map<String, Object>> getEmpGenderData();

    List<Map<String, Object>> getStudentsEducation();
}
