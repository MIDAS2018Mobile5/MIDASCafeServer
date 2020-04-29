package com.midas2018mobile5.serverapp.domain.user.userEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Neon K.I.D on 4/24/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email implements Serializable {
    @javax.validation.constraints.Email
    @Column(name = "email", nullable = false)
    private String value;

    @Builder
    public Email(String value) {
        this.value = value;
    }

    public static Email of(String email) {
        return new Email(email);
    }

    public String getHost() {
        int idx = value.indexOf("@");
        return value.substring(idx);
    }

    public String getId() {
        int idx = value.indexOf("@");
        return value.substring(0, idx);
    }
}
