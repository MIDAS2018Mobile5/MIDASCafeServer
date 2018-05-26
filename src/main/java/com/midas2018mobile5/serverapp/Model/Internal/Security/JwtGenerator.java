package com.midas2018mobile5.serverapp.Model.Internal.Security;

import com.midas2018mobile5.serverapp.Model.External.Account.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {

    public String generate(Account account) {
        Claims claims = Jwts.claims()
                .setSubject(account.getUsername());
        claims.put("userId", account.getUserid());
        claims.put("password", account.getPassword());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, "MIDAS")
                .compact();
    }
}
