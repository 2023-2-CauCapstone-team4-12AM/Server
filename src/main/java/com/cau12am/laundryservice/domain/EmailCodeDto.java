package com.cau12am.laundryservice.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailCodeDto {
    @NotBlank
    private String email;
    @NotBlank
    private String code;
}
