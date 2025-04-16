package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.global.security.domain.CustomUserDetails;

public interface OAuth2Service {
    CustomUserDetails login(String idToken);
}
