package com.cau12am.laundryservice.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    public String sendMail(String email, String title, String text){
        SimpleMailMessage emailForm = createEmailForm(email,title,text);
        Map<String, Object> result = new HashMap<>();
        try {
            javaMailSender.send(emailForm);
            return "메일 전송 완료";
        } catch (RuntimeException e) {
            log.info("메일 전송 실패");
            throw new RuntimeException(e);
        }
    }

    private SimpleMailMessage createEmailForm(String email, String title, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(title);
        message.setText(text);
        return message;
    }

}
