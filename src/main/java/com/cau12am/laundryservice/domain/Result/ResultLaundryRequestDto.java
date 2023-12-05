package com.cau12am.laundryservice.domain.Result;

import com.cau12am.laundryservice.domain.Laundry.LaundryRequest;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class ResultLaundryRequestDto {
    boolean success;
    String message;
    LaundryRequest laundryRequest;
}
