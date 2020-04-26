package com.midas2018mobile5.serverapp;

import com.midas2018mobile5.serverapp.domain.user.Privilege;
import com.midas2018mobile5.serverapp.domain.user.Role;
import com.midas2018mobile5.serverapp.domain.user.userEntity.Email;
import com.midas2018mobile5.serverapp.domain.user.userEntity.Password;
import com.midas2018mobile5.serverapp.domain.user.userEntity.User;
import com.midas2018mobile5.serverapp.repository.user.PrivilegeRepository;
import com.midas2018mobile5.serverapp.repository.user.RoleRepository;
import com.midas2018mobile5.serverapp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    private final PrivilegeRepository privilegeRepository;

    @Value("${property.app.init}")
    private boolean init;

    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!init)
            return;

        // PRIVILEGE
        Privilege orderPrivilege = createPrivilegeIfNotFound("ORDER_PRIVILEGE");
        Privilege adminPrivilege = createPrivilegeIfNotFound("ADMIN_PRIVILEGE");

        // ROLE
        List<Privilege> adminPrivileges = Arrays.asList(adminPrivilege, orderPrivilege);
        createRoleIfNotFound(Role.ROLES.ADMIN, adminPrivileges);
        createRoleIfNotFound(Role.ROLES.USER, Collections.singletonList(orderPrivilege));

        Role adminRole = roleRepository.findByName(Role.ROLES.ADMIN);
        User user = User.builder()
                .email(Email.of("admin@midas.kr"))
                .firstName("MIDASCafe")
                .lastName("Administrator")
                .roles(Collections.singletonList(adminRole))
                .userid("MIDASAdmin0527")
                .password(Password.builder().value("midasadmin1!").build())
                .build();
        userRepository.save(user);
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = Privilege.builder().name(name).build();
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = Role.builder().name(name).privileges(privileges).build();
            roleRepository.save(role);
        }
        return role;
    }
}
