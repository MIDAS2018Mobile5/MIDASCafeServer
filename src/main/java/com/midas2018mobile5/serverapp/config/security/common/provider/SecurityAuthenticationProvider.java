package com.midas2018mobile5.serverapp.config.security.common.provider;

import com.midas2018mobile5.serverapp.dao.user.UserService;
import com.midas2018mobile5.serverapp.domain.user.Role;
import com.midas2018mobile5.serverapp.domain.user.userEntity.User;
import com.midas2018mobile5.serverapp.dto.user.UserDto;
import com.midas2018mobile5.serverapp.repository.user.RolePermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Neon K.I.D on 4/29/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;
    private final RolePermissionRepository permissionRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication == null)
            throw new AuthenticationServiceException("No authentication data provided");

        String userid = authentication.getName();
        Object credentials = authentication.getCredentials();

        if (!(credentials instanceof String))
            throw new AuthenticationServiceException("Authentication credentials is not valid");

        User user = userService.validate(UserDto.SignInReq.builder()
                .userid(authentication.getName())
                .password(credentials.toString()).build());

        Collection<Role> roles = user.getRoles();
        if (roles.isEmpty())
            throw new BadCredentialsException("Authentication Failed. User granted authority is empty.");

        return new UsernamePasswordAuthenticationToken(userid, credentials.toString(), getGrantedAuthorities(roles));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private List<GrantedAuthority> getGrantedAuthorities(Collection<Role> roles) {
        List<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
        List<String> permissions = permissionRepository.permissions(roleIds);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        permissions.forEach(p -> grantedAuthorities.add(new SimpleGrantedAuthority(p)));

        return grantedAuthorities;
    }
}
