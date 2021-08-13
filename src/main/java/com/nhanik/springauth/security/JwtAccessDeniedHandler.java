package com.nhanik.springauth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhanik.springauth.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) throws IOException {
        response.setStatus(SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                objectMapper.writeValueAsString(
                        new ExceptionResponse(LocalDateTime.now(), SC_FORBIDDEN, e.getMessage())
                )
        );
    }
}
