package com.midas2018mobile5.serverapp.repository.user;

import com.midas2018mobile5.serverapp.domain.user.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Neon K.I.D on 4/26/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Privilege findByName(String name);
}
