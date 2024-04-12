package jpa.study.hellojpa.shoppingMallProject.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

        @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
            if("XMLHttpRequest".equals(request.getHeader("x-requested-with"))){
                // AJAX 요청인 경우 401 오류 반환
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            }else{
                // 일반 웹 브라우저 요청인 경우 로그인 페이지로 리디렉션
                response.sendRedirect("/members/login");
            }
    }
}
