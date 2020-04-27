package com.midas2018mobile5.serverapp.error.exception.user;

import lombok.Getter;

/**
 * Created by Neon K.I.D on 4/24/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Getter
public class UserNotFoundException extends RuntimeException {
    private long id;
    private String userId;

    public UserNotFoundException(long id) {
        this.id = id;
    }

    public UserNotFoundException(String userId) {
        this.userId = userId;
    }
}
