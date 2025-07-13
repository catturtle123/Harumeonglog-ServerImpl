package com.example.harumeonglog.domain.auth.service;

import com.example.harumeonglog.domain.auth.converter.OAuth2Converter;
import com.example.harumeonglog.domain.auth.dto.request.OAuth2Request;
import com.example.harumeonglog.domain.auth.dto.response.OAuth2Response;
import com.example.harumeonglog.domain.member.entity.enums.SocialType;
import com.example.harumeonglog.domain.member.repository.MemberRepository;
import com.example.harumeonglog.domain.member.repository.SettingRepository;
import com.example.harumeonglog.global.data.AppleOAuthConfigData;
import com.example.harumeonglog.global.error.exception.AuthException;
import com.example.harumeonglog.global.util.WebClientUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AppleOAuth2Service extends AbstractOAuth2Service {

    private static final String EMAIL = "email";
    private static final String NICKNAME = "name";

    private final WebClientUtil webClientUtil;
    private final AppleOAuthConfigData appleOAuthConfigData;

    public AppleOAuth2Service(MemberRepository memberRepository,
                              SettingRepository settingRepository,
                              WebClientUtil webClientUtil,
                              AppleOAuthConfigData appleOAuthConfigData) {
        super(memberRepository, settingRepository);
        this.webClientUtil = webClientUtil;
        this.appleOAuthConfigData = appleOAuthConfigData;
    }

    @Override
    protected OAuth2Response.OAuth2PublicKeyResponse getProperKeyInfo(OAuth2Response.OAuth2IdTokenHeader tokenHeader) {
        OAuth2Response.OAuth2PublicKeyListResponse keyList = getAppleWebClient().get()
                .retrieve()
                .bodyToMono(OAuth2Response.OAuth2PublicKeyListResponse.class)
                .block();
        return keyList != null ? keyList.getKeys().stream().filter(key -> key.getKid().equals(tokenHeader.getKid()) && key.getAlg().equals(tokenHeader.getAlg())).findAny().orElse(null) : null;
    }

    @Override
    protected OAuth2Request.OAuth2LoginRequest getLoginInfo(Claims claims) throws AuthException {
        String email = claims.get(EMAIL, String.class);
        return OAuth2Converter.toOAuth2LoginRequest(
                email,
                claims.get(NICKNAME, String.class) == null ? email.split("@")[0]: null,
                SocialType.APPLE,
                null,
                claims.getSubject()
        );
    }

    @Override
    protected boolean isValidLoginInfo(Claims claims) {
        return claims.getIssuer().equals(appleOAuthConfigData.getIss()) && claims.getAudience().contains(appleOAuthConfigData.getAppKey());
    }

    private WebClient getAppleWebClient() {
        return webClientUtil.getWebClient(appleOAuthConfigData.getPublicKeyUrl());
    }
}
