package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.converter.MemberBlockConverter;
import com.example.harumeonglog.domain.member.converter.MemberConverter;
import com.example.harumeonglog.domain.member.dto.request.MemberBlockRequest;
import com.example.harumeonglog.domain.member.dto.response.MemberBlockResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.MemberBlock;
import com.example.harumeonglog.domain.member.repository.MemberBlockRepository;
import com.example.harumeonglog.domain.member.repository.MemberRepository;
import com.example.harumeonglog.global.error.code.MemberErrorCode;
import com.example.harumeonglog.global.error.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberBlockCommandServiceImpl implements MemberBlockCommandService {

    private final MemberBlockRepository memberBlockRepository;
    private final MemberRepository memberRepository;

    @Override
    public MemberBlockResponse.MemberBlockInfoResponse updateBlock(Member member, MemberBlockRequest.UpdateMemberBlockRequest request) {
        Optional<MemberBlock> memberBlockOptional = memberBlockRepository.findByReporterAndReported(member.getId(), request.getReportedId());
        Boolean isBlock = false;

        if (memberBlockOptional.isPresent()) {
            memberBlockRepository.delete(memberBlockOptional.get());
        }
        else {
            MemberBlock memberBlock = MemberBlockConverter.toEntity(member, getMember(request.getReportedId()));
            memberBlockRepository.save(memberBlock);
            isBlock = true;
        }
        return MemberBlockConverter.toMemberBlockInfoResponse(member.getId(), request.getReportedId(), isBlock);
    }

    private Member getMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() ->
                new MemberException(MemberErrorCode.NOT_FOUND));
    }

}
