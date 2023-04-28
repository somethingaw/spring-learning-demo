package com.sfaw.springsecurityknife.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * MyAuthDenyEntryPoint
 * 当用户尝试访问需要权限才能的REST资源而不提供Token或者Token错误或者过期时，
 *
 * @author ArthurW
 * @version 1.0
 * @date 2023/4/27 14:08
 **/
public class MyAuthDenyEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("没有token或token过期");
        response.getWriter().flush();
    }
}
