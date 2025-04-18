package com.example.harumeonglog.domain.auth.service;

import com.example.harumeonglog.domain.auth.converter.OAuth2Converter;
import com.example.harumeonglog.domain.auth.dto.request.OAuth2Request;
import com.example.harumeonglog.domain.auth.dto.response.OAuth2Response;
import com.example.harumeonglog.domain.member.entity.enums.SocialType;
import com.example.harumeonglog.domain.member.repository.MemberRepository;
import com.example.harumeonglog.global.data.KakaoOAuthConfigData;
import com.example.harumeonglog.global.error.exception.AuthException;
import com.example.harumeonglog.global.util.WebClientUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KakaoOAuth2Service extends OAuth2ServiceImpl {

    private static final String EMAIL = "email";
    private static final String NICKNAME = "nickname";
    private static final String IMAGE = "picture";

    private final WebClientUtil webClientUtil;
    private final KakaoOAuthConfigData kakaoOAuthConfigData;

    public KakaoOAuth2Service(MemberRepository memberRepository,
                              WebClientUtil webClientUtil,
                              KakaoOAuthConfigData kakaoOAuthConfigData) {
        super(memberRepository);
        this.webClientUtil = webClientUtil;
        this.kakaoOAuthConfigData = kakaoOAuthConfigData;
    }

    @Override
    protected OAuth2Request.OAuth2LoginRequest getLoginInfo(Claims claims) throws AuthException {
        return OAuth2Converter.toOAuth2LoginRequest(
                claims.get(EMAIL, String.class),
                claims.get(NICKNAME, String.class),
                null,
                SocialType.KAKAO,
                claims.get(IMAGE, String.class),
                claims.getSubject()
        );
    }

    @Override
    public OAuth2Response.OAuth2PublicKeyResponse getProperKeyInfo(OAuth2Response.OAuth2IdTokenHeader tokenHeader) {
        OAuth2Response.OAuth2PublicKeyListResponse keyList = getPublicKeyWebClient().get()
                .retrieve()
                .bodyToMono(OAuth2Response.OAuth2PublicKeyListResponse.class)
                .block();
        return keyList != null ? keyList.getKeys().stream().filter(key -> key.getKid().equals(tokenHeader.getKid()) && key.getAlg().equals(tokenHeader.getAlg())).findAny().orElse(null) : null;
    }

    @Override
    protected boolean isValidLoginInfo(Claims claims) {
        return claims.getIssuer().equals(kakaoOAuthConfigData.getIss()) && claims.getAudience().contains(kakaoOAuthConfigData.getAppKey());
    }

    private WebClient getPublicKeyWebClient() {
        return webClientUtil.getWebClient(kakaoOAuthConfigData.getPublicKeyUrl());
    }
}
