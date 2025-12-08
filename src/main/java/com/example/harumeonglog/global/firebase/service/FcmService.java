package com.example.harumeonglog.global.firebase.service;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.NoticeType;
import com.example.harumeonglog.domain.member.service.MemberCommandService;
import com.example.harumeonglog.global.discord.DiscordApiUtil;
import com.example.harumeonglog.global.discord.dto.DiscordMessage;
import com.example.harumeonglog.global.firebase.converter.FCMConverter;
import com.example.harumeonglog.global.firebase.dto.request.FCMSendRequest;
import com.example.harumeonglog.global.firebase.dto.request.FCMSendRequest.ReceiverRequest;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final Environment environment;
    private final MemberCommandService memberCommandService;
    private final DiscordApiUtil discordApiUtil;

    @Async
    public void sendPushNotification(ReceiverRequest receiver, String title, String body, NoticeType noticeType) {
        if (receiver.getDeviceId() == null) {
            return;
        }

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        ApnsFcmOptions apnsFcmOptions = ApnsFcmOptions.builder()
                .setAnalyticsLabel(noticeType.toString())
                .build();

        ApnsConfig apnsConfig = ApnsConfig.builder()
                .setAps(Aps.builder()
                        .setAlert(ApsAlert.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build())
                        .setContentAvailable(true)
                        .build())
                .setFcmOptions(apnsFcmOptions)
                .build();

        Message message = Message.builder()
                .setNotification(notification)
                .setToken(receiver.getDeviceId())
                .setApnsConfig(apnsConfig)
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            handleFcmSendFailure(receiver, title, e);
        }
    }

    private void handleFcmSendFailure(ReceiverRequest receiver, String title, FirebaseMessagingException e) {
        memberCommandService.notDeadLockFcmSignOut(receiver.getReceiverId());

        boolean isLocalProfile = List.of(environment.getActiveProfiles()).contains("local");
        if (!isLocalProfile) {
            discordApiUtil.sendAlarm(buildDiscordErrorMessage(receiver, title, e));
        }
    }

    private DiscordMessage buildDiscordErrorMessage(ReceiverRequest receiver, String title, Exception e) {
        return DiscordMessage.builder()
                .content("# 🚨 FCM 알림 에러 발생")
                .embeds(List.of(
                        DiscordMessage.Embed.builder()
                                .title("ℹ️ 에러 정보")
                                .description(String.format(
                                        "%d번 회원의 FCM 토큰이 유효하지 않습니다.\n제목: %s\n에러: %s",
                                        receiver.getReceiverId(), title, getStackTrace(e).substring(0, Math.min(1000, getStackTrace(e).length()))
                                ))
                                .build()
                ))
                .build();
    }

    private String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
