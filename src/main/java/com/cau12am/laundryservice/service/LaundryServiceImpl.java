package com.cau12am.laundryservice.service;

import com.cau12am.laundryservice.domain.Laundry.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LaundryServiceImpl implements ILaundryService {
    private final LaundryInfoRepository laundryInfoRepository;
    private final LaundryRequestRepository laundryRequestRepository;
    @Override
    public List<LaundryInfo> findLaundry(String lon, String lat, String dis) {
        log.info("근처 코인세탁소 찾는 서비스 시작");

        Point point = new Point(Double.parseDouble(lon), Double.parseDouble(lat));
        Distance distance = new Distance(Double.parseDouble(dis), Metrics.KILOMETERS);

        List<LaundryInfo> nearLaundries = laundryInfoRepository.findByLocationNear( point, distance);

        return nearLaundries;
    }

    @Override
    public List<LaundryRequest> findLaundryALLRequests(String laundryId) {
        List<LaundryRequest> requests = laundryRequestRepository.findByLaundryId(laundryId);
        return requests;
    }

    @Override
    public List<LaundryRequest> findAllMyRequestsByEmail(String email) {
        List<LaundryRequest> requests = laundryRequestRepository.findByEmail(email);
        return requests;
    }

    @Override
    public LaundryRequest saveRequest(LaundryRequestDto laundryRequestDto) {
        LaundryRequest newData = LaundryRequest.builder()
                .laundryId(laundryRequestDto.getLaundryId())
                .email(laundryRequestDto.getEmail())
                .gender(laundryRequestDto.getGender())
                .clothesColorList(laundryRequestDto.getClothesColorList())
                .weight(laundryRequestDto.getWeight())
                .machineTypes(laundryRequestDto.getMachineTypes())
                .extraInfoType(laundryRequestDto.getExtraInfoType())
                .message(laundryRequestDto.getMessage())
                .date(new Date())
                .matched(false)
                .build();
        return laundryRequestRepository.save(newData);
    }

    @Override
    public void deleteRequest(String Id) {
        laundryRequestRepository.deleteById(Id);
    }

    @Override
    public Optional<LaundryRequest> findRequestById(String Id) {
        return laundryRequestRepository.findOne(
                Example.of(
                        LaundryRequest.builder()
                                ._id(Id)
                                .build()
                )
        );
    }

    @Override
    public Optional<LaundryRequest> updateRequest(LaundryRequestDto laundryRequestDto) {
        Optional<LaundryRequest> originalRequest = laundryRequestRepository.findById(laundryRequestDto.get_id());

        if(originalRequest.isEmpty()){
            return Optional.empty();
        } else {
            LaundryRequest newData = LaundryRequest.builder()
                    ._id(originalRequest.get().get_id())
                    .laundryId(laundryRequestDto.getLaundryId())
                    .email(laundryRequestDto.getEmail())
                    .gender(laundryRequestDto.getGender())
                    .clothesColorList(laundryRequestDto.getClothesColorList())
                    .weight(laundryRequestDto.getWeight())
                    .machineTypes(laundryRequestDto.getMachineTypes())
                    .extraInfoType(laundryRequestDto.getExtraInfoType())
                    .message(laundryRequestDto.getMessage())
                    .date(new Date())
                    .matched(false)
                    .build();
            return Optional.of(laundryRequestRepository.save(newData));
        }
    }
}
