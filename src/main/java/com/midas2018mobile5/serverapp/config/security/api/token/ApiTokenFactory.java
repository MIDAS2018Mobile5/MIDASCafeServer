package com.midas2018mobile5.serverapp.config.security.api.token;

import com.midas2018mobile5.serverapp.config.security.api.token.data.ApiTokenData;
import com.midas2018mobile5.serverapp.domain.user.Role;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Neon K.I.D on 4/27/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ApiTokenFactory {
    private final ApiTokenData apiTokenData;

    public String createToken(String username, Collection<Role> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("scopes", roles.stream().map(Object::toString).collect(Collectors.toList()));

        LocalDateTime now = LocalDateTime.now();

        return Jwts.builder().setClaims(claims)
                .setIssuer(apiTokenData.getIssuer())
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(now.plusMinutes(apiTokenData.getExpired())
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, apiTokenData.getSigningKey())
                .compact();
    }

    public String createRefreshToken(String username, Collection<Role> roles) {
        Claims claims = Jwts.claims().setSubject(username);

        List<String> scopes  = roles.stream().map(Object::toString).collect(Collectors.toList());
        scopes.add(ApiTokenData.AUTHORIZATION_REFRESH_TOKEN_NAME);
        claims.put("roles", scopes);

        LocalDateTime currentTime = LocalDateTime.now();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(apiTokenData.getIssuer())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime
                        .plusMinutes(apiTokenData.getRefreshExpired())
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, apiTokenData.getSigningKey())
                .compact();
    }

    public Map<String, Object> getBody(String token){
        return parseClaims(token).getBody();
    }

    public String extract(String header) {
        if (StringUtils.isEmpty(header))
            throw new AuthenticationServiceException("Authorization header cannot be blank!");

        if (header.length() <= ApiTokenData.AUTHORIZATION_HEADER_PREFIX.length())
            throw new AuthenticationServiceException("Invalid authorization header size.");

        return header.substring(ApiTokenData.AUTHORIZATION_HEADER_PREFIX.length());
    }

    private Jws<Claims> parseClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(apiTokenData.getSigningKey())
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            log.warn("Request token is expired. error={}", e.getMessage());
            throw new BadCredentialsException("Request token is expired.");
        } catch (Exception e) {
            log.error("Request token is invalid. token={}, error={}", token, e.getMessage());
            throw new BadCredentialsException("Request token is invalid.");
        }
    }
}
