package com.cau12am.laundryservice.domain.Result;

import com.cau12am.laundryservice.domain.Laundry.LaundryInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ResultLaundryInfoDto {
    boolean success;
    String message;
    List<LaundryInfo> laundryInfoList;
}
