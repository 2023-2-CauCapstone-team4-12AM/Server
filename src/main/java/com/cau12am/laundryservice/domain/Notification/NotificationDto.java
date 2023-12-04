package com.cau12am.laundryservice.domain.Notification;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
public class NotificationDto {
    private String email;
    private String title;
    private String body;
}
