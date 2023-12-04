package com.cau12am.laundryservice.domain.Notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "FCMToken")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FCMToken {
    @Id
    private String email;
    private String token;
}
