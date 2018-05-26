package com.midas2018mobile5.serverapp.Model.Internal.Security;

import com.midas2018mobile5.serverapp.Model.External.Account.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtValidator {
    private String secret = "MIDAS";

    public Account validate(String token) {
        Account account = null;
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            account = new Account();
            account.setUserid(body.getSubject());
            account.setRole((String)body.get("role"));
        } catch (Exception ex) {
            System.err.println(ex);
        }
        return account;
    }
}