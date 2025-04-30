package com.example.harumeonglog.domain.auth.service;

import com.example.harumeonglog.domain.auth.converter.AuthConverter;
import com.example.harumeonglog.domain.auth.dto.request.AuthRequest;
import com.example.harumeonglog.domain.auth.dto.response.AuthResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.SocialType;
import com.example.harumeonglog.global.error.code.AuthErrorCode;
import com.example.harumeonglog.global.error.code.TokenErrorCode;
import com.example.harumeonglog.global.error.exception.AuthException;
import com.example.harumeonglog.global.error.exception.TokenException;
import com.example.harumeonglog.global.security.domain.CustomUserDetails;
import com.example.harumeonglog.global.security.service.CustomDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthCommandServiceImpl implements AuthCommandService {

    private final TokenCommandService tokenCommandService;
    private final TokenQueryService tokenQueryService;
    private final CustomDetailService customDetailService;
    private final OAuth2Service appleOAuth2Service;
    private final OAuth2Service kakaoOAuth2Service;

    @Override
    public AuthResponse.AuthLoginResponse login(String provider, AuthRequest.AuthLoginRequest request) {
        CustomUserDetails userDetails;
        if (provider.equalsIgnoreCase(SocialType.KAKAO.name())) {
            userDetails = kakaoOAuth2Service.login(request.getIdToken());
        }
        else if (provider.equalsIgnoreCase(SocialType.APPLE.name())) {
            userDetails = appleOAuth2Service.login(request.getIdToken());
        }
        else {
            throw new AuthException(AuthErrorCode.UNSUPPORTED_PROVIDER);
        }
        return tokenCommandService.createToken(userDetails);
    }

    @Override
    public String logout(Member member) {
        return "로그아웃에 성공했습니다.";
    }

    @Override
    public AuthResponse.AuthAccessReissueResponse reissueAccess(AuthRequest.AuthAccessReissueRequest request) {
        String refreshToken = request.getRefreshToken();
        if (!tokenQueryService.isValid(refreshToken)) {
            throw new TokenException(TokenErrorCode.INVALID_TOKEN);
        }
        Long userId = tokenQueryService.getUserId(refreshToken);
        CustomUserDetails userDetails = customDetailService.loadUserById(userId);
        String accessToken = tokenCommandService.createAccessToken(userDetails);
        return AuthConverter.toAuthAccessReissueResponse(accessToken);
    }
}
