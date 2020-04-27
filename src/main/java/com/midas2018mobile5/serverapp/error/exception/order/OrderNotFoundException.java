package com.midas2018mobile5.serverapp.error.exception.order;

import lombok.Getter;

/**
 * Created by Neon K.I.D on 4/25/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Getter
public class OrderNotFoundException extends RuntimeException {
    private long id;

    public OrderNotFoundException(long id) {
        this.id = id;
    }
}
