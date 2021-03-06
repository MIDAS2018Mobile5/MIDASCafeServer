package com.midas2018mobile5.serverapp.config.security.common.handler;

import com.midas2018mobile5.serverapp.error.ErrorCode;
import com.midas2018mobile5.serverapp.error.ErrorResponse;
import com.midas2018mobile5.serverapp.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Neon K.I.D on 4/28/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Configuration
@Slf4j
public class AccessApiHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("FAILURE: " + exception.getMessage() + " for " + request.getMethod() + " " + request.getRequestURI());

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(JsonUtils.toJson(ErrorResponse.buildError(ErrorCode.TOKEN_EXPIRED)));
        response.getWriter().flush();
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("SUCCESS: " + request.getMethod() + " " + request.getRequestURI() + " for " + authentication.getName());
    }
}
