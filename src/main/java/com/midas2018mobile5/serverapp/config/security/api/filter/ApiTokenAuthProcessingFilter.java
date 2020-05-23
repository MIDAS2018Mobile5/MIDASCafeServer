package com.midas2018mobile5.serverapp.config.security.api.filter;

import com.midas2018mobile5.serverapp.config.security.SecurityConfig;
import com.midas2018mobile5.serverapp.config.security.api.token.ApiTokenFactory;
import com.midas2018mobile5.serverapp.config.security.api.token.data.ApiTokenData;
import com.midas2018mobile5.serverapp.config.security.common.handler.AccessApiHandler;
import com.midas2018mobile5.serverapp.dao.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Neon K.I.D on 4/27/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Slf4j
public class ApiTokenAuthProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private final UserService userService;
    private final AccessApiHandler accessApiHandler;
    private final ApiTokenFactory apiTokenFactory;

    public ApiTokenAuthProcessingFilter(
            SecurityConfig.SkipMatcher skipMatcher,
            UserService userRepository,
            AccessApiHandler securityUserLoginHandler,
            ApiTokenFactory apiTokenFactory
    ) {
        super(skipMatcher);

        this.userService = userRepository;
        this.accessApiHandler = securityUserLoginHandler;
        this.apiTokenFactory = apiTokenFactory;
    }

    @Transactional(readOnly = true)
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String accessToken
                = apiTokenFactory.extract(request.getHeader(ApiTokenData.AUTHORIZATION_HEADER_NAME));
        Map<String, Object> body = apiTokenFactory.getBody(accessToken);    // 토큰 검사
        String principal = (String) body.get("sub");    // 사용자 이름

        return userService.getAuthentication(principal);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        accessApiHandler.onAuthenticationSuccess(request, response, authResult);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        accessApiHandler.onAuthenticationFailure(request, response, failed);
    }
}
