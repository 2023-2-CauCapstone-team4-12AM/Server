package com.cau12am.laundryservice.domain;

import lombok.Data;

@Data
public class EmailPasswordDto {
    String email;
    String nowPW;
    String newPW;
}
