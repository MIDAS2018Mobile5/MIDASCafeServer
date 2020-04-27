package com.midas2018mobile5.serverapp.error.exception.cafe;

import lombok.Getter;

/**
 * Created by Neon K.I.D on 4/24/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Getter
public class CafeMenuDuplicationException extends RuntimeException {
    private String name;

    public CafeMenuDuplicationException(String menuName) {
        this.name = menuName;
    }
}
