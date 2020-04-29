package com.midas2018mobile5.serverapp.config.security;

import com.midas2018mobile5.serverapp.config.security.api.entrypoint.ApiTokenAuthEntryPoint;
import com.midas2018mobile5.serverapp.config.security.api.filter.ApiTokenAuthProcessingFilter;
import com.midas2018mobile5.serverapp.config.security.api.filter.SecurityUserLoginProcessingFilter;
import com.midas2018mobile5.serverapp.config.security.api.token.ApiTokenFactory;
import com.midas2018mobile5.serverapp.config.security.common.handler.AccessApiHandler;
import com.midas2018mobile5.serverapp.config.security.common.handler.SecurityUserLoginHandler;
import com.midas2018mobile5.serverapp.config.security.common.provider.SecurityAuthenticationProvider;
import com.midas2018mobile5.serverapp.config.security.common.repository.PersistTokenRepositoryImpl;
import com.midas2018mobile5.serverapp.dao.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Created by Neon K.I.D on 4/24/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final ApiTokenAuthEntryPoint apiTokenAuthEntryPoint;
    private final PersistTokenRepositoryImpl persistenceTokenRepository;
    private final UserService userService;
    private final AccessApiHandler accessApiHandler;
    private final SecurityUserLoginHandler securityUserLoginHandler;
    private final SecurityAuthenticationProvider securityAuthenticationProvider;
    private final ApiTokenFactory apiTokenFactory;

    private final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/webjars/**"
    };

    private static final String rememberKey = "remember-me";

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/api/**");
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(apiTokenAuthEntryPoint)
                .and()
                .addFilterBefore(buildApiUserLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildApiTokenAuthProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(securityAuthenticationProvider);
    }

    @Bean
    public PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices() {
        return new PersistentTokenBasedRememberMeServices(rememberKey, userService, persistenceTokenRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    protected SecurityUserLoginProcessingFilter buildApiUserLoginProcessingFilter() throws Exception {
        SecurityUserLoginProcessingFilter filter = new SecurityUserLoginProcessingFilter("/api/signIn",
                securityUserLoginHandler, persistentTokenBasedRememberMeServices());
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    protected ApiTokenAuthProcessingFilter buildApiTokenAuthProcessingFilter() throws Exception {
        SkipMatcher skipMatcher = new SkipMatcher("/api/**");
        ApiTokenAuthProcessingFilter filter = new ApiTokenAuthProcessingFilter(skipMatcher, userService,
                accessApiHandler, apiTokenFactory);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    public static class SkipMatcher implements RequestMatcher {
        private final OrRequestMatcher skipRequestMatcher;
        private final AntPathRequestMatcher antPathRequestMatcher;

        public SkipMatcher(String processUrl) {
            skipRequestMatcher = new OrRequestMatcher(
                    Arrays.asList(new AntPathRequestMatcher("/api/signIn"),
                            new AntPathRequestMatcher("/api/users/signUp"))
            );
            antPathRequestMatcher = new AntPathRequestMatcher(processUrl);
        }

        @Override
        public boolean matches(HttpServletRequest request) {
            if (skipRequestMatcher.matches(request))
                return false;
            return antPathRequestMatcher.matches(request);
        }
    }
}
