package com.cau12am.laundryservice.service;

import com.cau12am.laundryservice.domain.Laundry.LaundryInfo;

import java.util.List;
import java.util.Optional;

public interface ILaundryInfoService {
    List<LaundryInfo> findLaundry(String lan, String lat, String dis);
}
