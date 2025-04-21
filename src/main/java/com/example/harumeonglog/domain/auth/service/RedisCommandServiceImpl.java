package com.example.harumeonglog.domain.auth.service;

import com.example.harumeonglog.global.data.JwtConfigData;
import com.example.harumeonglog.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static com.example.harumeonglog.global.data.RedisConfigData.BLACKLIST_PREFIX;
import static com.example.harumeonglog.global.data.RedisConfigData.REFRESH_TOKEN_PREFIX;

@Service
@RequiredArgsConstructor
@Transactional
public class RedisCommandServiceImpl implements RedisCommandService{

    private final JwtConfigData jwtConfigData;
    private final RedisUtil redisUtil;

    public void addRefreshToken(Long id, String refresh) {
        redisUtil.save(REFRESH_TOKEN_PREFIX + id, refresh, Duration.ofMillis(jwtConfigData.getTime().getRefresh()));
    }

    public void addBlackList(String token) {
        redisUtil.save(BLACKLIST_PREFIX + token, true, Duration.ofMillis(jwtConfigData.getTime().getAccess()));
    }

    public void deleteRefreshToken(Long userId) {
        redisUtil.delete(REFRESH_TOKEN_PREFIX + userId);
    }

    public void deleteBlackList(String access) {
        redisUtil.delete(BLACKLIST_PREFIX + access);
    }
}
