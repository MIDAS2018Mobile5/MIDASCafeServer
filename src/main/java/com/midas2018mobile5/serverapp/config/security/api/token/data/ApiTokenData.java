package com.midas2018mobile5.serverapp.config.security.api.token.data;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Neon K.I.D on 4/27/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "security.token")
public class ApiTokenData {
    private String issuer;

    private Long expired;

    private Long refreshExpired;

    private String signingKey;

    public static final String AUTHORIZATION_REFRESH_TOKEN_NAME = "REFRESH_TOKEN";

    public static final String AUTHORIZATION_HEADER_NAME  = "Authorization";

    public static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";
}
