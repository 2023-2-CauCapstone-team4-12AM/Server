package com.cau12am.laundryservice.domain;

import lombok.Data;

@Data
public class ResultWithTokenDto {
    boolean success;
    String message;
    String refreshToken;
    String accessToken;
}
