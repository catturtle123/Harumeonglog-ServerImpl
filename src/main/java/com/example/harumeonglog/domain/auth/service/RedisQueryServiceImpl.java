package com.example.harumeonglog.domain.auth.service;

import com.example.harumeonglog.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.harumeonglog.global.data.RedisConfigData.BLACKLIST_PREFIX;
import static com.example.harumeonglog.global.data.RedisConfigData.REFRESH_TOKEN_PREFIX;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RedisQueryServiceImpl implements RedisQueryService{

    private final RedisUtil redisUtil;

    public boolean isBlackList(String token) {
        return Boolean.TRUE.equals(redisUtil.hasKey(BLACKLIST_PREFIX + token));
    }

    public String getRefreshToken(Long id) {
        return (String) redisUtil.get(REFRESH_TOKEN_PREFIX + id);
    }
}
