package com.cau12am.laundryservice.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMember {
    @Id
    private String _id;
    private String pw;
    private String email;
    private String name;
    private String studentID;
    private String sex;
    private String refreshToken;
}
