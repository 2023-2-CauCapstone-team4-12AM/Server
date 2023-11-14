package com.cau12am.laundryservice.domain;

import lombok.Data;

@Data
public class UserMemberDto {
    private Long _id;
    private String pw;
    private String email;
    private String name;
    private String studentID;
    private String sex;
    private String refreshToken;
}
