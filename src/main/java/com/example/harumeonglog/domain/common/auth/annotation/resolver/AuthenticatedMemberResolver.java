package com.example.harumeonglog.domain.common.auth.annotation.resolver;

import com.example.harumeonglog.domain.common.auth.annotation.AuthenticatedMember;
import com.example.harumeonglog.domain.common.auth.domain.CustomUserDetails;
import com.example.harumeonglog.domain.member.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class AuthenticatedMemberResolver implements HandlerMethodArgumentResolver {

    private final SecurityContextRepository securityContextRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedMember.class) && parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        UserDetails userDetails = (UserDetails) securityContextRepository.loadDeferredContext(webRequest.getNativeRequest(HttpServletRequest.class)).get().getAuthentication().getPrincipal();
        if (userDetails instanceof CustomUserDetails customUserDetails) {
            return customUserDetails.getLoginMember();
        }
        return null;
    }
}
