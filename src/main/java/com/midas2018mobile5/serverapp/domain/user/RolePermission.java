package com.midas2018mobile5.serverapp.domain.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Neon K.I.D on 4/26/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "ROLE_PERMISSION")
public class RolePermission implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String permission;

//    @ManyToMany(mappedBy = "privileges", fetch = FetchType.EAGER)
//    private Collection<Role> roles;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Override
    public String getAuthority() {
        return permission;
    }

    @Builder
    public RolePermission(String name, Role role) {
        this.permission = name;
        this.role = role;
    }

    public static class PERMISSIONS {
        public static final String USER = "USER_PRIVILEGE";
        public static final String ADMIN = "ADMIN_PRIVILEGE";
    }
}
