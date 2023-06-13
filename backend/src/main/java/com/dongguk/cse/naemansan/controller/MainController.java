package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.dto.request.NotificationRequestDto;
import com.dongguk.cse.naemansan.service.FirebaseCloudMessageService;
import com.dongguk.cse.naemansan.service.IosNotificationService;
import com.dongguk.cse.naemansan.service.NotificationService;
import com.google.firebase.iid.FirebaseInstanceId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
//이벤트 처리하고 삭제 예정

@RestController
@RequiredArgsConstructor
public class MainController {
    //private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final NotificationService notificationService;
    private final IosNotificationService iosNotificationService;
    //userid,
    @PostMapping("/api/fcm")
    public ResponseEntity pushMessage(Authentication authentication, @RequestBody NotificationRequestDto notificationRequestDto) throws IOException {
        System.out.println(notificationRequestDto.getTargetToken() + " "
                + notificationRequestDto.getTitle() + " " + notificationRequestDto.getContent());
/*
        notificationService.sendMessageTo(
                notificationRequestDto.getTargetToken(),
                notificationRequestDto.getTitle(),
                notificationRequestDto.getContent());
*/
//notificationRequestDto 수정
        //notificationService.createNotification(Long.valueOf(authentication.getName()),notificationRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/iosfcm")
    public void pushIosMessage(@RequestBody String token) throws Exception{
        iosNotificationService.sendApnFcmtoken(token);
    }
}