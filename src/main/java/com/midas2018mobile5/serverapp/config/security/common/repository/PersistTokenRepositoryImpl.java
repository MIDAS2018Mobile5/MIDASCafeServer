package com.midas2018mobile5.serverapp.config.security.common.repository;

import com.midas2018mobile5.serverapp.config.security.common.repository.vo.RememberToken;
import com.midas2018mobile5.serverapp.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Neon K.I.D on 4/27/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class PersistTokenRepositoryImpl implements PersistentTokenRepository {
    // Redis DB
    private final StringRedisTemplate stringRedisTemplate;

    // Username: ID, Series: series, Token: token, Date: last_used
    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        RememberToken rememberToken
                = new RememberToken(token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate());
        stringRedisTemplate.opsForValue().set(token.getSeries(), JsonUtils.toJson(rememberToken), 30, TimeUnit.DAYS);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        String payload = stringRedisTemplate.opsForValue().get(series);
        try {
            RememberToken rememberToken = JsonUtils.fromJson(payload, RememberToken.class);
            rememberToken.setTokenValue(tokenValue);
            rememberToken.setDate(lastUsed);

            stringRedisTemplate.opsForValue().set(series, JsonUtils.toJson(rememberToken), 30, TimeUnit.DAYS);
            log.debug("Remember me token is updated. seried={}", series);
        } catch (Exception ex) {
            log.error("Persistent token is not valid. payload={}, error={}", payload, ex);
        }
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        String payload = stringRedisTemplate.opsForValue().get(seriesId);
        if (payload == null) return null;
        try {
            RememberToken rememberToken = JsonUtils.fromJson(payload, RememberToken.class);
            return new PersistentRememberMeToken(
                    rememberToken.getUsername(),
                    seriesId,
                    rememberToken.getTokenValue(),
                    rememberToken.getDate());
        } catch (Exception ex) {
            log.error("Persistent token is not valid. payload={}, error={}", payload, ex);
            return null;
        }
    }

    @Override
    public void removeUserTokens(String username) {

    }
}
