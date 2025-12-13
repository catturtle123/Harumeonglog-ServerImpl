package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.converter.MemberConverter;
import com.example.harumeonglog.domain.member.dto.request.MemberRequest;
import com.example.harumeonglog.domain.member.dto.response.MemberResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.repository.MemberRepository;
import com.example.harumeonglog.global.error.code.MemberErrorCode;
import com.example.harumeonglog.global.error.code.S3ErrorCode;
import com.example.harumeonglog.global.error.exception.MemberException;
import com.example.harumeonglog.global.error.exception.S3Exception;
import com.example.harumeonglog.global.util.OutboxUtil;
import com.example.harumeonglog.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final S3Util s3Util;
    private final OutboxUtil outboxUtil;

    @Override
    public MemberResponse.MemberInfoUpdateResponse updateInfo(Member member, MemberRequest.MemberInfoUpdateRequest request) {
        Member updatedMember = memberRepository.findById(member.getId()).orElseThrow(() ->
                new MemberException(MemberErrorCode.NOT_FOUND));
        updatedMember.update(request.getNickname(), request.getImageKey());

        // Outbox 상태 변경
        if (request.getImageKey() != null && !request.getImageKey().isEmpty()) {
            if (!s3Util.isObjectExists(request.getImageKey())) {
                throw new S3Exception(S3ErrorCode.NOT_FOUND);
            }
            outboxUtil.changeS3OutboxStatus(request.getImageKey());
        }

        return MemberConverter.toMemberInfoUpdateResponse(updatedMember, s3Util);
    }

    @Override
    public void softDeleteMember(Member member) {
        Member deletedMember = memberRepository.findById(member.getId()).orElseThrow(() ->
                new MemberException(MemberErrorCode.NOT_FOUND));
        deletedMember.softDelete();
    }

    @Override
    public Member changeMemberTerms(Member member, MemberRequest.MemberTermsUpdateRequest request) {
        Member updatedMember = memberRepository.findById(member.getId()).orElseThrow(() ->
                new MemberException(MemberErrorCode.NOT_FOUND));
        updatedMember.updateTerms(request.getDidAgree());
        return updatedMember;
    }

    @Override
    public void saveFCM(Member member, MemberRequest.FCMRequest fcmRequest) {
        member.updateFCMToken(fcmRequest.getFcmToken());
        memberRepository.save(member);
    }

    @Override
    public void notDeadLockFcmSignOut(Long member) {
        memberRepository.updateDeviceIdByMemberId(member);
    }
}
