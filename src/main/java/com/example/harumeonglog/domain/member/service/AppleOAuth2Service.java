package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.dto.request.OAuth2Request;
import com.example.harumeonglog.domain.member.dto.response.OAuth2Response;
import com.example.harumeonglog.domain.member.infrastructure.MemberRepository;
import com.example.harumeonglog.global.error.exception.AuthException;
import com.example.harumeonglog.global.util.WebClientUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class AppleOAuth2Service extends OAuth2ServiceImpl {

    private final WebClientUtil webClientUtil;
    public AppleOAuth2Service(MemberRepository memberRepository,
                              WebClientUtil webClientUtil) {
        super(memberRepository);
        this.webClientUtil = webClientUtil;
    }

    @Override
    protected OAuth2Response.OAuth2PublicKeyResponse getProperKeyInfo(OAuth2Response.OAuth2IdTokenHeader tokenHeader) {
        return null;
    }

    @Override
    protected OAuth2Request.OAuth2LoginRequest getLoginInfo(Claims claims) throws AuthException {
        return null;
    }

    @Override
    protected boolean isValidLoginInfo(Claims claims) {
        return false;
    }
}
