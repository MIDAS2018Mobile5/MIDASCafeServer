package com.midas2018mobile5.serverapp.config.security.api.filter;

import com.midas2018mobile5.serverapp.config.security.SecurityConfig;
import com.midas2018mobile5.serverapp.config.security.api.token.ApiTokenFactory;
import com.midas2018mobile5.serverapp.config.security.api.token.data.ApiTokenData;
import com.midas2018mobile5.serverapp.config.security.common.handler.SecurityUserLoginHandler;
import com.midas2018mobile5.serverapp.dao.user.UserService;
import com.midas2018mobile5.serverapp.domain.user.Role;
import com.midas2018mobile5.serverapp.domain.user.userEntity.User;
import com.midas2018mobile5.serverapp.error.exception.user.UserNotFoundException;
import com.midas2018mobile5.serverapp.repository.user.RolePermissionRepository;
import com.midas2018mobile5.serverapp.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Neon K.I.D on 4/27/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Slf4j
public class ApiTokenAuthProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private final UserService userService;
    private final SecurityUserLoginHandler securityUserLoginHandler;
    private final ApiTokenFactory apiTokenFactory;

    public ApiTokenAuthProcessingFilter(
            SecurityConfig.SkipMatcher skipMatcher,
            UserService userRepository,
            SecurityUserLoginHandler securityUserLoginHandler,
            ApiTokenFactory apiTokenFactory
    ) {
        super(skipMatcher);

        this.userService = userRepository;
        this.securityUserLoginHandler = securityUserLoginHandler;
        this.apiTokenFactory = apiTokenFactory;
    }

    // Search JWT Auth info
    @Transactional(readOnly = true)
    public Authentication getAuthentication(String principal) {
        UserDetails details = userService.loadUserByUsername(principal);

        if (details.getAuthorities().isEmpty())
            throw new BadCredentialsException("Authentication Failed. User granted authority is empty.");

        log.info("Api user attempt authentication. username={}, grantedAuthorities={}", principal, details.getAuthorities());

        return new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
    }

    @Transactional(readOnly = true)
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String accessToken
                = apiTokenFactory.extract(request.getHeader(ApiTokenData.AUTHORIZATION_HEADER_NAME));
        Map<String, Object> body = apiTokenFactory.getBody(accessToken);
        String principal = (String) body.get("sub");

        return getAuthentication(principal);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        securityUserLoginHandler.onAuthenticationFailure(request, response, failed);
    }
}
