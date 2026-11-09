package com.study.service;

public interface LoginService {

    /**
     * 登录成功，签发令牌
     *
     * @param username 登录账号
     */
    String login(String username);

    /**
     * 解析令牌，返回用户名
     *
     * @return 令牌无效或已过期时返回 null
     */
    String getUsername(String token);

    /**
     * 退出登录（JWT 无状态，服务端不需要清理会话）
     */
    void logout(String token);
}
