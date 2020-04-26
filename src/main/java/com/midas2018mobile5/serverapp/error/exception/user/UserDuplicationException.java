package com.midas2018mobile5.serverapp.error.exception.user;

import lombok.Getter;

/**
 * Created by Neon K.I.D on 4/24/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Getter
public class UserDuplicationException extends RuntimeException {
    private String userid;
    private String field;

    public UserDuplicationException(String userid) {
        this.userid = userid;
        this.field = "userid";
    }
}
