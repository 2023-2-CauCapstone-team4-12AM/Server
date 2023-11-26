package com.cau12am.laundryservice.domain.Result;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResultDto {
    boolean success;
    String message;
}
