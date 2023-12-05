package com.cau12am.laundryservice.service;

import com.cau12am.laundryservice.domain.Notification.FCMToken;
import com.cau12am.laundryservice.domain.Notification.FCMTokenDto;
import com.cau12am.laundryservice.domain.Notification.FCMTokenRepository;
import com.cau12am.laundryservice.domain.Notification.NotificationDto;
import com.cau12am.laundryservice.domain.Result.ResultDto;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FCMNotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final FCMTokenRepository fcmTokenRepository;

    public boolean saveToken(FCMTokenDto fcmTokenDto){
        try{
            fcmTokenRepository.save(FCMToken.builder().token(fcmTokenDto.getToken()).email(fcmTokenDto.getEmail()).build());
            return true;
        } catch (DataAccessException e){
            return false;
        }
    }

    public boolean deleteToken(String email){
        try{
            fcmTokenRepository.deleteById(email);
            return true;
        } catch (DataAccessException e){
            return false;
        }
    }

    public ResultDto sendNotification(String email, String title, String body){
        Optional<FCMToken> tokenInfo = fcmTokenRepository.findById(email);

        if(tokenInfo.isEmpty()){
            return ResultDto.builder().message("상대방의 fcm토근이 존재하지 않습니다.").success(false).build();
        }

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(tokenInfo.get().getToken())
                .setNotification(notification)
                .build();

        try{
            firebaseMessaging.send(message);
            return ResultDto.builder().message("알람 보내기 성공").success(true).build();
        } catch (FirebaseMessagingException e) {
            return ResultDto.builder().message("알람 보내기 실패").success(false).build();
        }

    }

}
