package com.study.service.impl;

import com.study.mapper.EmpMapper;
import com.study.pojo.JobOption;
import com.study.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceimpl implements ReportService {
    @Autowired
    private EmpMapper empMapper;
    @Override
    public JobOption getEmpJobData() {
       //1.调用Mapper接口，获取统计数据
        List<Map<String,Object>> list = empMapper.countEmpJobData();
        //2.组装数据
       List<String> jobList = list.stream().map(dataMap->dataMap.get("position").toString()).toList();
       List<Object> dataList=list.stream().map(dataMap->dataMap.get("num")).toList();
        return new JobOption(jobList,dataList);
    }

    @Override
    public List<Map<String, Object>> getEmpGenderData() {
        return empMapper.countEmpGenderData();
    }
}
