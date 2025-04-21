package com.example.harumeonglog.domain.auth.service;

import com.example.harumeonglog.domain.auth.converter.OAuth2Converter;
import com.example.harumeonglog.domain.auth.dto.request.OAuth2Request;
import com.example.harumeonglog.domain.auth.dto.response.OAuth2Response;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.Setting;
import com.example.harumeonglog.domain.member.repository.MemberRepository;
import com.example.harumeonglog.domain.member.repository.SettingRepository;
import com.example.harumeonglog.global.error.code.AuthErrorCode;
import com.example.harumeonglog.global.error.code.TokenErrorCode;
import com.example.harumeonglog.global.error.exception.AuthException;
import com.example.harumeonglog.global.error.exception.TokenException;
import com.example.harumeonglog.global.security.domain.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Optional;

import static org.apache.tomcat.util.codec.binary.Base64.decodeBase64;

@RequiredArgsConstructor
public abstract class OAuth2ServiceImpl implements OAuth2Service {

    private final MemberRepository memberRepository;
    private final SettingRepository settingRepository;

    @Override
    public CustomUserDetails login(String idToken) {
        OAuth2Response.OAuth2IdTokenHeader tokenHeader = parseIdTokenHeader(idToken);
        OAuth2Response.OAuth2PublicKeyResponse keyInfo = getProperKeyInfo(tokenHeader);
        if (keyInfo == null) {
            throw new TokenException(TokenErrorCode.INVALID_ID_TOKEN);
        }
        PublicKey publicKey = generatePublicKey(keyInfo);

        Claims claims = parseIdToken(idToken, publicKey);

        if (!isValidLoginInfo(claims)) {
            throw new AuthException(AuthErrorCode.INVALID_ISSUER);
        }

        OAuth2Request.OAuth2LoginRequest loginRequest = getLoginInfo(claims);
        return successLogin(loginRequest);
    }

    protected abstract OAuth2Response.OAuth2PublicKeyResponse getProperKeyInfo(OAuth2Response.OAuth2IdTokenHeader tokenHeader);

    protected abstract OAuth2Request.OAuth2LoginRequest getLoginInfo(Claims claims) throws AuthException;

    protected abstract boolean isValidLoginInfo(Claims claims);

    protected CustomUserDetails successLogin(OAuth2Request.OAuth2LoginRequest request) {
        Optional<Member> memberOptional = memberRepository.findByProviderIdAndSocialType(request.getProviderId(), request.getSocialType());
        Member member;
        if (memberOptional.isPresent()) {
            member = memberOptional.get();
            // TODO: 로그인 시 동기화 여부
            member.update(request.getNickname(), request.getImage());
        }
        else {
            member = memberRepository.save(OAuth2Converter.toMember(request));
            createSetting(member);
        }

        return new CustomUserDetails(member);
    }

    protected OAuth2Response.OAuth2IdTokenHeader parseIdTokenHeader(String idToken) {
        String header = idToken.split("\\.")[0];

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(decodeBase64(header), OAuth2Response.OAuth2IdTokenHeader.class);
        } catch (IOException e) {
            throw new TokenException(TokenErrorCode.INVALID_ID_TOKEN);
        }
    }

    protected PublicKey generatePublicKey(OAuth2Response.OAuth2PublicKeyResponse keyInfo) {
        final byte[] nBytes = Base64.getUrlDecoder().decode(keyInfo.getN());
        final byte[] eBytes = Base64.getUrlDecoder().decode(keyInfo.getE());

        final RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(1, nBytes), new BigInteger(1, eBytes));

        try {
            return KeyFactory.getInstance(keyInfo.getKty()).generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new TokenException(TokenErrorCode.FAIL_PARSING_ID_TOKEN);
        }
    }

    private Claims parseIdToken(String idToken, PublicKey publicKey) {
        try {
            return Jwts.parser()
                    .verifyWith(publicKey)
                    .clockSkewSeconds(60)
                    .build()
                    .parseSignedClaims(idToken)
                    .getPayload();
        } catch (RuntimeException e) {
            throw new TokenException(TokenErrorCode.INVALID_ID_TOKEN);
        }
    }

    private Setting createSetting(Member member) {
        return settingRepository.save(Setting.builder()
                .morningAlarm(true)
                .articleLikeAlarm(true)
                .eventAlarm(true)
                .commentAlarm(true)
                .member(member)
                .build());
    }

}
