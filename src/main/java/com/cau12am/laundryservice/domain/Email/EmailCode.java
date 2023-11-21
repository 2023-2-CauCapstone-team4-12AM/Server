package com.cau12am.laundryservice.domain.Email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "email_code")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailCode {
    @Id
    private String email;
    private String code;
    private boolean check;
}
