package com.midas2018mobile5.serverapp.repository.user;

import com.midas2018mobile5.serverapp.domain.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Neon K.I.D on 4/26/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
