package com.midas2018mobile5.serverapp.config.security.api.token;

import com.midas2018mobile5.serverapp.config.security.api.token.data.ApiTokenData;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

    public String createToken(Authentication authentication) {
        String username = authentication.getName();
        if (StringUtils.isEmpty(username))
            throw new UsernameNotFoundException("Can't create token without username");

        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) authentication.getAuthorities();
        if (grantedAuthorities == null || grantedAuthorities.isEmpty())
            throw new BadCredentialsException("User doesn't have any privileges");

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("scopes", grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        LocalDateTime now = LocalDateTime.now();

        return Jwts.builder().setClaims(claims)
                .setIssuer(apiTokenData.getIssuer())
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(now.plusMinutes(apiTokenData.getExpired())
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, apiTokenData.getSigningKey())
                .compact();
    }

    public String createRefreshToken(Authentication authentication) {
        String username = authentication.getName();
        if (StringUtils.isEmpty(username))
            throw new UsernameNotFoundException("Can't create token without username");

        Claims claims = Jwts.claims().setSubject(username);
        List<String> scopes  = getGrantedAuthorities(authentication).stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        scopes.add(ApiTokenData.AUTHORIZATION_REFRESH_TOKEN_NAME);
        claims.put("scopes", scopes);

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

    public List<? extends GrantedAuthority> getGrantedAuthorities(Authentication authentication) {
        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) authentication.getAuthorities();
        if (grantedAuthorities == null || grantedAuthorities.isEmpty())
            throw new BadCredentialsException("User doesn't have any privileges");
        return grantedAuthorities;
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

    // Token 유효성 체크
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
