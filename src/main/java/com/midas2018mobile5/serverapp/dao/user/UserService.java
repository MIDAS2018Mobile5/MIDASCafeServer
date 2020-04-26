package com.midas2018mobile5.serverapp.dao.user;

import com.midas2018mobile5.serverapp.domain.user.Role;
import com.midas2018mobile5.serverapp.domain.user.userEntity.User;
import com.midas2018mobile5.serverapp.dto.user.UserDto;
import com.midas2018mobile5.serverapp.error.exception.user.UserDuplicationException;
import com.midas2018mobile5.serverapp.error.exception.user.UserNotFoundException;
import com.midas2018mobile5.serverapp.error.exception.user.UserPasswordInvalidException;
import com.midas2018mobile5.serverapp.repository.user.RoleRepository;
import com.midas2018mobile5.serverapp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by Neon K.I.D on 4/24/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

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

        Role userRole = roleRepository.findByName(Role.ROLES.USER);
        return userRepository.save(dto.toEntity(userRole));
    }

    @Transactional(readOnly = true)
    public User validate(UserDto.SignInReq dto) {
        User user = findByUserId(dto.getUserid());

        if (!user.getPwd().isMatched(dto.getPassword()))
            throw new UserPasswordInvalidException(dto.getPassword());

        return user;
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
