package com.cau12am.laundryservice.controller;

import com.cau12am.laundryservice.domain.Notification.FCMTokenDto;
import com.cau12am.laundryservice.domain.Notification.NotificationDto;
import com.cau12am.laundryservice.domain.Result.ResultDto;
import com.cau12am.laundryservice.service.FCMNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private final FCMNotificationService fcmNotificationService;
    @PostMapping("/saveToken")
    public ResponseEntity<ResultDto> saveFCMToken(@RequestBody FCMTokenDto fcmTokenDto){
        log.info("fcm 토큰 등록 컨트롤러 시작");

        boolean result = fcmNotificationService.saveToken(fcmTokenDto);

        if(!result) {
            return new ResponseEntity<>(ResultDto.builder().success(false).message("실패").build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(ResultDto.builder().success(true).message("성공").build(), HttpStatus.OK);
    }

    @DeleteMapping("/deleteToken")
    public ResponseEntity<ResultDto> deleteFCMToken(@RequestParam(value = "email") String email){
        log.info("fcm 토큰 삭제 컨트롤러 시작");

        boolean result = fcmNotificationService.deleteToken(email);

        if(!result) {
            return new ResponseEntity<>(ResultDto.builder().success(false).message("실패").build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(ResultDto.builder().success(true).message("성공").build(), HttpStatus.OK);
    }

    @PostMapping("/notice")
    public ResponseEntity<ResultDto> noticeMessage(@RequestBody NotificationDto notificationDto){
        log.info("fcm 알림보내는 컨트롤러 시작");

        ResultDto result = fcmNotificationService.sendNotification(notificationDto);

        if(!result.isSuccess()) {
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
