package com.study.controller;

import com.study.pojo.Result;
import com.study.service.impl.LogServiceimpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 操作日志查询接口：GET /logs
 */
@RestController
@Slf4j
public class LogController {

    @Autowired
    private LogServiceimpl logService;

    @GetMapping("/logs")
    public Result list() {
        return Result.success(logService.list());
    }
}
