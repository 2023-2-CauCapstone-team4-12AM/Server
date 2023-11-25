package com.cau12am.laundryservice.service;

import com.cau12am.laundryservice.domain.Laundry.LaundryInfo;
import com.cau12am.laundryservice.domain.Laundry.LaundryRequest;
import com.cau12am.laundryservice.domain.Laundry.LaundryRequestDto;

import java.util.List;
import java.util.Optional;

public interface ILaundryService {
    List<LaundryInfo> findLaundry(String lan, String lat, String dis);
    List<LaundryRequest> findLaundryALLRequests(String laundryId);

    List<LaundryRequest> findAllMyRequestsByEmail(String email);

    LaundryRequest saveRequest(LaundryRequestDto laundryRequestDto);
    void deleteRequest(String Id);

    Optional<LaundryRequest> findRequestById(String Id);
    Optional<LaundryRequest> updateRequest(LaundryRequestDto laundryRequestDto);
}
