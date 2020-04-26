package com.midas2018mobile5.serverapp.error.exception.user;

import lombok.Getter;

/**
 * Created by Neon K.I.D on 4/25/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Getter
public class UserPasswordInvalidException extends RuntimeException {
    private String password;

    public UserPasswordInvalidException(String password) {
        this.password = password;
    }
}
