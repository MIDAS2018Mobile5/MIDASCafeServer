package com.midas2018mobile5.serverapp.dto.user;

import com.midas2018mobile5.serverapp.domain.user.Role;
import com.midas2018mobile5.serverapp.domain.user.userEntity.Email;
import com.midas2018mobile5.serverapp.domain.user.userEntity.Password;
import com.midas2018mobile5.serverapp.domain.user.userEntity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Neon K.I.D on 4/24/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class UserDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUpReq {
        @Valid
        private Email email;

        @Valid
        private String firstName;

        @Valid
        private String lastName;

        @Valid
        private String password;

        @Valid
        private String userid;

        @Builder
        public SignUpReq(String userid, Email email, String firstName,
                         String lastName, String password) {
            this.userid = userid;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.password = password;
        }

        public User toEntity() {
            return User.builder()
                    .userid(this.userid)
                    .email(this.email)
                    .firstName(this.firstName)
                    .lastName(this.lastName)
                    .password(Password.builder().value(this.password).build())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignInReq {
        @Valid
        public String userid;

        @Valid
        public String password;

        @Builder
        public SignInReq(String userid, String password) {
            this.userid = userid;
            this.password = password;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MyUserRequest {
        @Valid
        private Email email;

        @Valid
        private String oldPassword;

        @Valid
        private String newPassword;

        @Valid
        private String firstName;

        @Valid
        private String lastName;

        @Builder
        public MyUserRequest(final Email email, String oldPassword,
                             String newPassword, String firstName, String lastName) {
            this.email = email;
            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

    @Getter
    public static class Res {
        private String userid;
        private String firstName;
        private String lastName;
        private Email email;

        public Res(User user) {
            this.userid = user.getUserid();
            this.email = user.getEmail();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
        }
    }

    @Getter
    public static class SignInRes {
        private User user;
        private String token;
        private Collection<? extends GrantedAuthority> authorities;

        @Builder
        public SignInRes(User user, String token) {
            this.user = user;
            this.token = token;
            this.authorities = user.getRoles();
        }
    }
}
