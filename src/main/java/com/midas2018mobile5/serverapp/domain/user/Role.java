package com.midas2018mobile5.serverapp.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.midas2018mobile5.serverapp.domain.user.userEntity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Neon K.I.D on 4/24/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "roles_privileges",
//            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id")
//    )
//    private Collection<RolePermission> privileges;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<RolePermission> privileges;

    @JsonIgnore
    @Override
    public String getAuthority() {
        return name;
    }

    @Builder
    public Role(String name, User user, Collection<RolePermission> privileges) {
        this.name = name;
        this.user = user;
        this.privileges = privileges;
    }

    public static class ROLES {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
