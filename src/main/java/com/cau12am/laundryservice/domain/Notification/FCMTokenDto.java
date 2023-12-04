package com.cau12am.laundryservice.domain.Notification;

import lombok.Data;

@Data
public class FCMTokenDto {
    private String email;
    private String token;
}
