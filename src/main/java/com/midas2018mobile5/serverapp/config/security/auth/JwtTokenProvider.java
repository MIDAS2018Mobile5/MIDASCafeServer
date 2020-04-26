package com.midas2018mobile5.serverapp.config.security.auth;

import com.midas2018mobile5.serverapp.dao.user.UserService;
import com.midas2018mobile5.serverapp.domain.user.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

/**
 * Created by Neon K.I.D on 4/26/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private final UserService userService;

    private String key = "MIDAS";

    // Token expired time
    private long tokenValidTime = 30 * 60 * 1000L;

    @PostConstruct
    protected void init() {
        key = Base64.getEncoder().encodeToString(key.getBytes());
    }

    // Generate JWT token
    public String createToken(String username, Collection<Role> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);

        Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder().setClaims(claims)
                .setIssuedAt(now).setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    // Search JWT Auth info
    @Transactional(readOnly = true)
    public Authentication getAuthentication(String token) {
        UserDetails details = userService.loadUserByUsername(getUserId(token));
        return new UsernamePasswordAuthenticationToken(details, "", details.getAuthorities());
    }

    // Get User info in token
    public String getUserId(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
    }

    // Get header for token value
    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("X-AUTH-TOKEN");
    }

    // Check expired
    public boolean validateToken(String jwtToken) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken);
        return !claims.getBody().getExpiration().before(new Date());
    }
}
