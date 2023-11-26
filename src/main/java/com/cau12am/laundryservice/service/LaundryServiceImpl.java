package com.cau12am.laundryservice.service;

import com.cau12am.laundryservice.domain.Laundry.*;
import com.cau12am.laundryservice.domain.Match.MatchRepository;
import com.cau12am.laundryservice.domain.Result.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        return laundryInfoRepository.findByLocationNear( point, distance);
    }

    @Override
    public List<LaundryRequest> findLaundryALLRequests(String laundryId) {
        return laundryRequestRepository.findByLaundryIdAndMatchedIsFalse(laundryId);
    }

    @Override
    public List<LaundryRequest> findAllMyRequestsByEmail(String email) {
        return laundryRequestRepository.findByEmail(email);
    }

    @Override
    public LaundryRequest saveRequest(LaundryRequestDto laundryRequestDto) {
        LaundryRequest newData = LaundryRequest.builder()
                .laundryId(laundryRequestDto.getLaundryId())
                .email(laundryRequestDto.getEmail())
                .gender(laundryRequestDto.getGender())
                .colorTypes(laundryRequestDto.getColorTypes())
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
    public ResultDto deleteRequest(String Id) {
        Optional<LaundryRequest> oneById = laundryRequestRepository.findOneBy_id(Id);

        if(oneById.isPresent()){
            if(!oneById.get().isMatched()){
                laundryRequestRepository.deleteById(Id);
                return ResultDto.builder().message("성공").success(true).build();
            }
            return ResultDto.builder().message("매칭되어있습니다.").success(false).build();
        }
        return ResultDto.builder().message("존재하지않습니다.").success(false).build();
    }

    @Override
    public Optional<LaundryRequest> findRequestById(String Id) {
        return laundryRequestRepository.findOneBy_id(Id);
    }

    @Override
    public Optional<LaundryRequest> updateRequest(LaundryRequestDto laundryRequestDto) {
        Optional<LaundryRequest> originalRequest = laundryRequestRepository.findById(laundryRequestDto.get_id());

        if(originalRequest.isEmpty()){
            return Optional.empty();
        }

        LaundryRequest origin = originalRequest.get();

        if(origin.isMatched()){
            return Optional.empty();
        }

        LaundryRequest newData = LaundryRequest.builder()
                ._id(origin.get_id())
                .laundryId(origin.getLaundryId())
                .email(origin.getEmail())
                .gender(origin.getGender())
                .colorTypes(laundryRequestDto.getColorTypes())
                .weight(laundryRequestDto.getWeight())
                .machineTypes(laundryRequestDto.getMachineTypes())
                .extraInfoType(laundryRequestDto.getExtraInfoType())
                .message(laundryRequestDto.getMessage())
                .date(origin.getDate())
                .matched(origin.isMatched())
                .version(origin.getVersion())
                .build();

        return Optional.of(laundryRequestRepository.save(newData));
    }


}
