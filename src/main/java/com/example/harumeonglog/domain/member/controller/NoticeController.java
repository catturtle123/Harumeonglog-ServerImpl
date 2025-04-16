package com.example.harumeonglog.domain.member.controller;

import com.example.harumeonglog.domain.member.entity.Notice;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.domain.member.dto.response.NoticeResponse;
import com.example.harumeonglog.domain.member.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/notices")
    public CustomResponse<NoticeResponse.NoticeListResponse> getNotices(
            @RequestParam(name = "size") Integer size,
            @RequestParam(name = "cursor") Long cursor
    ) {
        Slice<Notice> noticeSlice = noticeService.getNotices(size, cursor);
        Long nextCursor = noticeSlice.toList().get(noticeSlice.getSize() -1).getId();
        return CustomResponse.ok(NoticeResponse.NoticeListResponse.from(nextCursor, noticeSlice.hasNext(), noticeSlice.stream().toList()));
    }


    @DeleteMapping("/notices/{noticeId}")
    public CustomResponse<Void> deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return CustomResponse.ok(null);
    }

}
