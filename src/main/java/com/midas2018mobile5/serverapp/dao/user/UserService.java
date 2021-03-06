package com.midas2018mobile5.serverapp.dao.user;

import com.midas2018mobile5.serverapp.domain.user.Role;
import com.midas2018mobile5.serverapp.domain.user.RolePermission;
import com.midas2018mobile5.serverapp.domain.user.userEntity.User;
import com.midas2018mobile5.serverapp.dto.user.UserDto;
import com.midas2018mobile5.serverapp.error.exception.user.UserDuplicationException;
import com.midas2018mobile5.serverapp.error.exception.user.UserNotFoundException;
import com.midas2018mobile5.serverapp.error.exception.user.UserPasswordInvalidException;
import com.midas2018mobile5.serverapp.repository.user.RolePermissionRepository;
import com.midas2018mobile5.serverapp.repository.user.RoleRepository;
import com.midas2018mobile5.serverapp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by Neon K.I.D on 4/24/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository permRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUserId(username);
    }

    @Transactional(readOnly = true)
    public User findById(long id) {
        final Optional<User> user = userRepository.findById(id);
        user.orElseThrow(() -> new UserNotFoundException(id));
        return user.get();
    }

    @Transactional(readOnly = true)
    public User findByUserId(String userid) {
        final User user = userRepository.findByUserid(userid);
        if (user == null) throw new UserNotFoundException(userid);
        return user;
    }

    @Transactional(readOnly = true)
    public boolean isExistedUser(String userid) {
        return userRepository.findByUserid(userid) != null;
    }

    public User create(UserDto.SignUpReq dto) {
        if (isExistedUser(dto.getUserid()))
            throw new UserDuplicationException(dto.getUserid());

        User user = dto.toEntity();
        Role userRole = Role.builder().user(user).name(Role.ROLES.USER).build();
        RolePermission userPerm = RolePermission.builder()
                .name(RolePermission.PERMISSIONS.USER)
                .role(userRole).build();

        user = userRepository.save(user);
        roleRepository.save(userRole);
        permRepository.save(userPerm);

        return user;
    }

    @Transactional(readOnly = true)
    public User validate(UserDto.SignInReq dto) {
        User user = findByUserId(dto.getUserid());

        if (!user.getPwd().isMatched(dto.getPassword()))
            throw new UserPasswordInvalidException(dto.getPassword());

        return user;
    }

    // Search JWT Auth info
    @Transactional(readOnly = true)
    public Authentication getAuthentication(String principal) {
        UserDetails details = loadUserByUsername(principal);

        if (details.getAuthorities().isEmpty())
            throw new BadCredentialsException("Authentication Failed. User granted authority is empty.");

        Object[] authorities = details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray();

        log.info("Api user attempt authentication. username={}, grantedAuthorities={}", principal, Arrays.toString(authorities));

        return new UsernamePasswordAuthenticationToken(details.getUsername(), null, details.getAuthorities());
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    public void deleteByUserId(String userid) {
        final User user = findByUserId(userid);
        userRepository.delete(user);
    }

    public User updateById(long id, UserDto.MyUserRequest dto) {
        final User user = findById(id);
        user.update(dto);
        return user;
    }

    public User updateByUserId(String userid, UserDto.MyUserRequest dto) {
        final User user = findByUserId(userid);
        user.update(dto);
        return user;
    }
}
