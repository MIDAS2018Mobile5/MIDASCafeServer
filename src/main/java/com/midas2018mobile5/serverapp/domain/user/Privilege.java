package com.midas2018mobile5.serverapp.domain.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
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
@Table
public class Privilege implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToMany(mappedBy = "privileges", fetch = FetchType.EAGER)
    private Collection<Role> roles;

    @Override
    public String getAuthority() {
        return name;
    }

    @Builder
    public Privilege(String name) {
        this.name = name;
    }
}
