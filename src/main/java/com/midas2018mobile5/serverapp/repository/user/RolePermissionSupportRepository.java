package com.midas2018mobile5.serverapp.repository.user;

import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Neon K.I.D on 4/27/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public interface RolePermissionSupportRepository {
    List<String> permissions(@Param("roleIds") List<Long> roleIds);
}
