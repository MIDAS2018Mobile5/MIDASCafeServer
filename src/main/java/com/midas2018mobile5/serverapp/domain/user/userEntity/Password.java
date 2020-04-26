package com.midas2018mobile5.serverapp.domain.user.userEntity;

import com.midas2018mobile5.serverapp.error.exception.user.UserPasswordInvalidException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

/**
 * Created by Neon K.I.D on 4/24/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {
    @Column(name = "password", nullable = false)
    private String value;

    @Column(name = "password_exp_date")
    private LocalDateTime expDate;

    @Column(name = "password_ttl")
    private long ttl;

    @Builder
    public Password(final String value) {
        this.ttl = 1209_604;
        this.value = encodePassword(value);
        this.expDate = extendExpDate();
    }

    public boolean isMatched(final String rawPassword) {
        return isMatches(rawPassword);
    }

    public void changePassword(final String newPassword, final String oldPassword) {
        if (isMatched(oldPassword)) {
            value = encodePassword(newPassword);
            expDate = extendExpDate();
        } else throw new UserPasswordInvalidException(oldPassword);
    }

    private LocalDateTime extendExpDate() {
        return LocalDateTime.now().plusSeconds(ttl);
    }

    private boolean isMatches(String rawPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword, this.value);
    }

    private String encodePassword(final String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
