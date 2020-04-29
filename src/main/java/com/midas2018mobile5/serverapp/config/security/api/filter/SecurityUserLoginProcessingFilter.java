package com.midas2018mobile5.serverapp.config.security.api.filter;

import com.midas2018mobile5.serverapp.config.security.common.handler.SecurityUserLoginHandler;
import com.midas2018mobile5.serverapp.dao.user.UserService;
import com.midas2018mobile5.serverapp.domain.user.userEntity.User;
import com.midas2018mobile5.serverapp.dto.user.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Neon K.I.D on 4/28/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Slf4j
public class SecurityUserLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    public SecurityUserLoginProcessingFilter(
            String processUrl,
            UserService userService,
            SecurityUserLoginHandler securityUserLoginHandler,
            PersistentTokenBasedRememberMeServices rememberMeServices
    ) {
        super(processUrl);

        this.userService = userService;
        this.securityUserLoginHandler = securityUserLoginHandler;
        this.rememberMeServices = rememberMeServices;
    }

    private final UserService userService;
    private final SecurityUserLoginHandler securityUserLoginHandler;
    private final PersistentTokenBasedRememberMeServices rememberMeServices;

    @Transactional(readOnly = true)
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        if (!HttpMethod.POST.name().equals(request.getMethod()))
            throw new AuthenticationServiceException("Authentication method not supported");

        String userid = request.getParameter("userid");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userid, password);
        log.info("User attempt authentication. username={}", userid);
        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        rememberMeServices.loginSuccess(request, response, authResult);
        securityUserLoginHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        securityUserLoginHandler.onAuthenticationFailure(request, response, failed);
    }
}
