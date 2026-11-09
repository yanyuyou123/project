package com.study.interceptor;

import com.study.service.impl.LogServiceimpl;
import com.study.service.impl.LoginServiceimpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录校验拦截器：
 * 在请求进入 Controller 前校验请求头 token，未登录则返回 401。
 * 同时记录操作日志。
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginServiceimpl loginService;

    @Autowired
    private LogServiceimpl logService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 记录开始时间，供 afterCompletion 计算耗时
        request.setAttribute("startTime", System.currentTimeMillis());

        String token = request.getHeader("token");
        // 一次解析即可：有效返回用户名，无效/过期返回 null
        String username = loginService.getUsername(token);
        if (username == null) {
            response.setStatus(401);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"code\":0,\"msg\":\"未登录或登录已过期，请重新登录\"}");
            return false; // 拦截，不再进入 Controller
        }
        // 把用户名放进请求域，供日志使用
        request.setAttribute("loginUser", username);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long start = request.getAttribute("startTime") == null
                ? System.currentTimeMillis()
                : (Long) request.getAttribute("startTime");
        long cost = System.currentTimeMillis() - start;
        String user = (String) request.getAttribute("loginUser");
        logService.record(request.getMethod(), request.getRequestURI(), user, cost, response.getStatus());
    }
}
