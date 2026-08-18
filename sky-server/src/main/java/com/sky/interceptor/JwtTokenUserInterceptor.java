package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.properties.DevProperties;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private DevProperties  devProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是 Controller 方法，直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

//        log.info("开发模式：模拟用户 ID 为 1");
//        BaseContext.setCurrentId(1L);
//        return true;

        // 以下是正式 token 校验逻辑（开发模式下不会执行到这里）
        // 1. 从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getUserTokenName());
        if (token == null || token.isEmpty()) {
            token = request.getHeader("token");
        }

        // 2. 校验令牌
        try {
            log.info("用户端 jwt 校验：{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("当前用户 id：{}", userId);

            // 3. 存入 ThreadLocal（用户端用）
            BaseContext.setCurrentId(userId);
            return true;

        } catch (Exception ex) {
            log.error("用户端 jwt 校验失败：{}", ex.getMessage());
            // 4. 不通过，响应 401 状态码
            response.setStatus(401);
            return false;
        }


    }
}