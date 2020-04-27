package com.midas2018mobile5.serverapp;

import com.midas2018mobile5.serverapp.domain.user.RolePermission;
import com.midas2018mobile5.serverapp.domain.user.Role;
import com.midas2018mobile5.serverapp.domain.user.userEntity.Email;
import com.midas2018mobile5.serverapp.domain.user.userEntity.Password;
import com.midas2018mobile5.serverapp.domain.user.userEntity.User;
import com.midas2018mobile5.serverapp.repository.user.RolePermissionRepository;
import com.midas2018mobile5.serverapp.repository.user.RoleRepository;
import com.midas2018mobile5.serverapp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Neon K.I.D on 4/26/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@RequiredArgsConstructor
@Component
public class AppStartupRunner implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository privilegeRepository;

    @Value("${property.app.init}")
    private boolean init;

    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!init)
            return;

        // CREATE ADMINISTRATOR
        User user = User.builder()
                .email(Email.of("admin@midas.kr"))
                .firstName("MIDASCafe")
                .lastName("Administrator")
                .userid("MIDASAdmin0527")
                .password(Password.builder().value("midasadmin1!").build())
                .build();
        userRepository.save(user);

        // ADMIN ROLE
        Role adminRole = createRoleIfNotFound(Role.ROLES.ADMIN, user);

        // ADMIN PRIVILEGE
        RolePermission adminPrivilege = RolePermission.builder()
                .name(RolePermission.PERMISSIONS.ADMIN)
                .role(adminRole).build();

        privilegeRepository.save(adminPrivilege);
    }

    @Transactional
    Role createRoleIfNotFound(String name, User user) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = Role.builder().name(name).user(user).build();
            roleRepository.save(role);
        }
        return role;
    }
}
