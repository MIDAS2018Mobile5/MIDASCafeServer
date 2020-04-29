package com.midas2018mobile5.serverapp.config.security.api.token;

import com.midas2018mobile5.serverapp.config.security.api.token.data.ApiTokenData;
import com.midas2018mobile5.serverapp.config.security.api.token.data.RefreshToken;
import com.midas2018mobile5.serverapp.dao.user.UserService;
import com.midas2018mobile5.serverapp.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Neon K.I.D on 4/29/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@RestController
@RequiredArgsConstructor
public class TokenController {
    private final PersistentTokenBasedRememberMeServices rememberMeService;
    private final ApiTokenFactory apiTokenFactory;

//    @PreAuthorize("isAuthenticated()")
//    @PostMapping(value = "/signOut")
//    @ResponseStatus(value = HttpStatus.OK)
//    public ResponseEntity<?> signOutUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
//                                         HttpServletRequest req, HttpServletResponse res) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        if (auth != null) {
//            rememberMeService.logout(req, res, auth);
//            new SecurityContextLogoutHandler().logout(req, res, auth);
//        }
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/api/token")
    @ResponseStatus(value = HttpStatus.OK)
    public RefreshToken refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String accessToken = apiTokenFactory.extract(token);
        Assert.hasLength(accessToken, "Request token is invalid.");

        Map<String, Object> body = apiTokenFactory.getBody(accessToken);
        List<String> scopes = (List<String>) body.get("scopes");

        if (scopes.isEmpty() || !scopes.contains(ApiTokenData.AUTHORIZATION_REFRESH_TOKEN_NAME))
            throw new IllegalArgumentException("Request token is not 'REFRESH_TOKEN'.");

        scopes.remove(ApiTokenData.AUTHORIZATION_REFRESH_TOKEN_NAME);

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        scopes.forEach(r -> grantedAuthorities.add(new SimpleGrantedAuthority(r)));

        String username = (String) body.get("sub");

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
        String newAccessToken = apiTokenFactory.createToken(authentication);
        String newRefreshAccessToken = apiTokenFactory.createRefreshToken(authentication);

        return RefreshToken.builder().token(newAccessToken).refreshToken(newRefreshAccessToken).build();
    }
}
