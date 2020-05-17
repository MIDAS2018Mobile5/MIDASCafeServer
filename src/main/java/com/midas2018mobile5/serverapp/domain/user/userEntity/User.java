package com.midas2018mobile5.serverapp.domain.user.userEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.midas2018mobile5.serverapp.domain.order.Mcorder;
import com.midas2018mobile5.serverapp.domain.user.Role;
import com.midas2018mobile5.serverapp.dto.user.UserDto;
import com.midas2018mobile5.serverapp.model.DateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Neon K.I.D on 4/24/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table
public class User extends DateTime implements UserDetails {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Who are you ?")
    @Size(min = 5, max = 120)
    @Column(name = "userid", unique = true)
    private String userid;

    @Embedded
    @JsonIgnore
    private Password password;

    @NotNull(message = "Enter your firstName...")
    @Column
    private String firstName;

    @NotNull(message = "Enter your lastName...")
    @Column
    private String lastName;

    @Embedded
    private Email email;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Mcorder> orders = new LinkedHashSet<>();

//    @JsonIgnore
//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "users_roles",
//            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
//    private Collection<Role> roles;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Role> roles;

    @Builder
    public User(String userid, Password password, String firstName,
                Collection<Role> roles, String lastName, Email email) {
        this.userid = userid;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
    }

    public void update(UserDto.MyUserRequest dto) {
        if (dto.getEmail() != null)
            this.email = dto.getEmail();

        if (dto.getFirstName() != null)
            this.firstName = dto.getFirstName();

        if (dto.getLastName() != null)
            this.lastName = dto.getLastName();

        if (dto.getNewPassword() != null)
            this.password.changePassword(dto.getNewPassword(), dto.getOldPassword());
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getUsername() {
        return userid;
    }

    public String getPassword() { return password.getValue(); }

    @JsonIgnore
    public Password getPwd() { return password; }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
