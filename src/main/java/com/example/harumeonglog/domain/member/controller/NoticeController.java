package com.example.harumeonglog.domain.member.controller;

import com.example.harumeonglog.domain.member.controller.specification.NoticeControllerSpecification;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.service.NoticeCommandService;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.domain.member.dto.response.NoticeResponse;
import com.example.harumeonglog.domain.member.service.NoticeQueryService;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class NoticeController implements NoticeControllerSpecification {

    private final NoticeQueryService noticeQueryService;
    private final NoticeCommandService noticeCommandService;

    @GetMapping("/notices")
    public CustomResponse<NoticeResponse.NoticeListResponse> getNotices(
            @AuthenticatedMember Member member,
            @RequestParam(name = "cursor") Long cursor,
            @RequestParam(name = "size") Integer size
    ) {
        NoticeResponse.NoticeListResponse noticeListResponse = noticeQueryService.getNotices(member, size, cursor);
        return CustomResponse.ok(noticeListResponse);
    }

    @DeleteMapping("/notices/{noticeId}")
    public CustomResponse<Void> deleteNotice(@PathVariable Long noticeId) {
        noticeCommandService.deleteNotice(noticeId);
        return CustomResponse.ok(null);
    }

}
