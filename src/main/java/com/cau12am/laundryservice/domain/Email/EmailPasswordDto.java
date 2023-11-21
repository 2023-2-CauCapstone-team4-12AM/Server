package com.cau12am.laundryservice.domain.Email;

import lombok.Data;

@Data
public class EmailPasswordDto {
    String email;
    String nowPW;
    String newPW;
}
