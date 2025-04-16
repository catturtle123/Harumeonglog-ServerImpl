package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.converter.MemberConverter;
import com.example.harumeonglog.domain.member.dto.request.MemberRequest;
import com.example.harumeonglog.domain.member.dto.response.MemberResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.SocialType;
import com.example.harumeonglog.global.error.code.AuthErrorCode;
import com.example.harumeonglog.global.error.exception.AuthException;
import com.example.harumeonglog.global.security.domain.CustomUserDetails;
import com.example.harumeonglog.global.util.JwtUtil;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Builder
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final JwtUtil jwtUtil;
    private final OAuth2Service appleOAuth2Service;
    private final OAuth2Service kakaoOAuth2Service;

    @Override
    public MemberResponse.MemberLoginResponse login(String provider, MemberRequest.MemberLoginRequest request) {
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
        return MemberConverter.toMemberLoginResponse(userDetails.getLoginMember().getId(), jwtUtil.createAccessToken(userDetails), jwtUtil.createRefreshToken(userDetails));
    }

    @Override
    public MemberResponse.MemberLogoutResponse logout(Member member) {
        return null;
    }

    @Override
    public Member updateInfo(Member member, MemberRequest.MemberInfoUpdateRequest request) {
        return null;
    }
}
