package com.midas2018mobile5.serverapp.repository.user;

import com.midas2018mobile5.serverapp.domain.user.QRolePermission;
import com.midas2018mobile5.serverapp.domain.user.RolePermission;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

/**
 * Created by Neon K.I.D on 4/27/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class RolePermissionRepositoryImpl extends QuerydslRepositorySupport implements RolePermissionSupportRepository {
    public RolePermissionRepositoryImpl() {
        super(RolePermission.class);
    }

    @Override
    public List<String> permissions(List<Long> roleIds) {
        final QRolePermission qrp = QRolePermission.rolePermission;
        final JPQLQuery<RolePermission> query = from(qrp).where(qrp.role.id.in(roleIds));

        return query.select(qrp.permission).fetch();
    }
}
