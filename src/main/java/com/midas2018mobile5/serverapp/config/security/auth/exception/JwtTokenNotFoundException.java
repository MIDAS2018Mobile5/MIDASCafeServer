package com.midas2018mobile5.serverapp.config.security.auth.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by Neon K.I.D on 4/26/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Getter
public class JwtTokenNotFoundException extends AuthenticationException {
    public JwtTokenNotFoundException() {
        super("JWT Token is not found...");
    }
}
