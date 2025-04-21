package com.example.harumeonglog.domain.auth.service;

import com.example.harumeonglog.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenQueryServiceImpl implements TokenQueryService{

    private final JwtUtil jwtUtil;
    private final RedisQueryService redisQueryService;

    @Override
    public boolean isValid(String token) {
        return !redisQueryService.isBlackList(token) && jwtUtil.isValid(token);
    }

    @Override
    public Long getUserId(String token) {
        return jwtUtil.getUserId(token);
    }
}
