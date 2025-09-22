package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.converter.MemberBlockConverter;
import com.example.harumeonglog.domain.member.dto.response.MemberBlockResponse;
import com.example.harumeonglog.domain.member.repository.MemberBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberBlockQueryServiceImpl implements MemberBlockQueryService {

    private final MemberBlockRepository memberBlockRepository;

    @Override
    public MemberBlockResponse.MemberBlockInfoResponse isBlock(Long reporterId, Long reportedId) {
        return MemberBlockConverter.toMemberBlockInfoResponse(
                reporterId,
                reportedId,
                memberBlockRepository.existsByReporterAndReported(reporterId, reportedId)
        );
    }
}
