package com.cau12am.laundryservice.service;

import com.cau12am.laundryservice.domain.Laundry.LaundryInfo;
import com.cau12am.laundryservice.domain.Laundry.LaundryRequest;
import com.cau12am.laundryservice.domain.Laundry.LaundryRequestDto;
import com.cau12am.laundryservice.domain.Result.ResultDto;
import com.cau12am.laundryservice.domain.Result.ResultLaundryRequestDto;

import java.util.List;
import java.util.Optional;

public interface ILaundryService {
    List<LaundryInfo> findLaundry(String lan, String lat, String dis);
    List<LaundryRequest> findLaundryALLRequests(String email, String laundryId);

    List<LaundryRequest> findAllMyRequestsByEmail(String email);

    LaundryRequest saveRequest(LaundryRequestDto laundryRequestDto);
    ResultDto deleteRequest(String Id);

    Optional<LaundryRequest> findRequestById(String Id);
    ResultLaundryRequestDto updateRequest(LaundryRequestDto laundryRequestDto);

}
