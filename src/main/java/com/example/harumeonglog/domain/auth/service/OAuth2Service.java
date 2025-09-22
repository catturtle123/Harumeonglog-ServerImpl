package com.example.harumeonglog.domain.auth.service;

import com.example.harumeonglog.domain.auth.dto.response.OAuth2Response;
import com.example.harumeonglog.global.security.domain.CustomUserDetails;

public interface OAuth2Service {
    OAuth2Response.OAuth2LoginSuccessResponse login(String idToken);
}
