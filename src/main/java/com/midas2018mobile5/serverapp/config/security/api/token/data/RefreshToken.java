package com.midas2018mobile5.serverapp.config.security.api.token.data;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by Neon K.I.D on 4/29/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Getter
public class RefreshToken {
    private String token;
    private String refreshToken;

    @Builder
    public RefreshToken(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
