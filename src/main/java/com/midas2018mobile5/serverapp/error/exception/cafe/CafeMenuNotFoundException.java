package com.midas2018mobile5.serverapp.error.exception.cafe;

import lombok.Getter;

/**
 * Created by Neon K.I.D on 4/25/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Getter
public class CafeMenuNotFoundException extends RuntimeException {
    private long id;
    private String menuName;

    public CafeMenuNotFoundException(long id) {
        this.id = id;
    }

    public CafeMenuNotFoundException(String menuName) {
        this.menuName = menuName;
    }
}
