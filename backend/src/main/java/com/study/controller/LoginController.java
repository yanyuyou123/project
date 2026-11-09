package com.study.controller;

import com.study.mapper.EmpMapper;
import com.study.pojo.Emp;
import com.study.pojo.Result;
import com.study.service.impl.LoginServiceimpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 登录接口：POST /login（校验用户名密码，成功下发 token）
 *        退出：POST /logout（从请求头取 token 并移除）
 */
@RestController
@Slf4j
public class LoginController {

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private LoginServiceimpl loginService;

    @PostMapping("/login")
    public Result login(@RequestBody Emp emp) {
        log.info("登录请求: {}", emp.getUsername());
        // 按用户名密码查询员工（演示项目密码明文存储，生产需加密）
        Emp dbEmp = empMapper.login(emp.getUsername(), emp.getPassword());
        if (dbEmp == null) {
            return Result.error("用户名或密码错误");
        }
        // 登录成功，签发 JWT（subject 用账号，不使用中文姓名）
        String token = loginService.login(dbEmp.getUsername());
        return Result.success(token);
    }

    @PostMapping("/logout")
    public Result logout(@RequestHeader(value = "token", required = false) String token) {
        loginService.logout(token);
        return Result.success();
    }
}
