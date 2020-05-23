package com.midas2018mobile5.serverapp.dto.user;

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
import java.util.Date;

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
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
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
        private final String userid;
        private final String firstName;
        private final String lastName;
        private final Email email;

        public Res(User user) {
            this.userid = user.getUserid();
            this.email = user.getEmail();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
        }
    }

    @Getter
    public static class SignInRes {
        private final MCtoken token;
        private final MCtoken refreshToken;
        private final Collection<? extends GrantedAuthority> authorities;

        @Builder
        public SignInRes(String token, Date tokenExpired,
                         String refreshToken, Date refreshTokenExpired,
                         Collection<? extends GrantedAuthority> authorities) {
            this.token = MCtoken.builder().value(token).expired(tokenExpired).build();
            this.refreshToken = MCtoken.builder().value(refreshToken).expired(refreshTokenExpired).build();
            this.authorities = authorities;
        }
    }

    @Getter
    private static class MCtoken {
        private final String value;
        private final Date expired;

        @Builder
        public MCtoken(String value, Date expired) {
            this.value = value;
            this.expired = expired;
        }
    }
}
