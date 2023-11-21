package com.cau12am.laundryservice.controller;

import com.cau12am.laundryservice.domain.Laundry.LaundryInfo;
import com.cau12am.laundryservice.domain.Laundry.UserPlaceDto;
import com.cau12am.laundryservice.domain.Result.ResultLaundryInfoDto;
import com.cau12am.laundryservice.service.ILaundryInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/laundry-info")
public class LaundryInfoController {

    private final ILaundryInfoService iLaundryInfoService;
    @PostMapping("/findNear")
    public ResponseEntity<ResultLaundryInfoDto> findNearLaundry(@Valid @RequestBody UserPlaceDto userPlaceDto){

        log.info("근처 코인세탁소 찾는 컨트롤러 시작");

        List<LaundryInfo> laundries = iLaundryInfoService.findLaundry(
                userPlaceDto.getLon(), userPlaceDto.getLat(), userPlaceDto.getDistance());

        ResultLaundryInfoDto result = ResultLaundryInfoDto.builder()
                .success(true)
                .message("성공")
                .laundryInfoList(laundries)
                .build();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
