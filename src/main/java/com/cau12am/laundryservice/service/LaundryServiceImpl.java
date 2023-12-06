package com.cau12am.laundryservice.service;

import com.cau12am.laundryservice.domain.Ban.Ban;
import com.cau12am.laundryservice.domain.Ban.BanRepository;
import com.cau12am.laundryservice.domain.Laundry.*;
import com.cau12am.laundryservice.domain.Match.MatchRepository;
import com.cau12am.laundryservice.domain.Result.ResultDto;
import com.cau12am.laundryservice.domain.Result.ResultLaundryRequestDto;
import com.cau12am.laundryservice.domain.User.UserMember;
import com.cau12am.laundryservice.domain.User.UserMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LaundryServiceImpl implements ILaundryService {
    private final LaundryInfoRepository laundryInfoRepository;
    private final LaundryRequestRepository laundryRequestRepository;
    private final BanRepository banRepository;
    @Override
    public List<LaundryInfo> findLaundry(String lon, String lat, String dis) {
        log.info("근처 코인세탁소 찾는 서비스 시작");

        Point point = new Point(Double.parseDouble(lon), Double.parseDouble(lat));
        Distance distance = new Distance(Double.parseDouble(dis), Metrics.KILOMETERS);

        return laundryInfoRepository.findByLocationNear( point, distance);
    }

    @Override
    public List<LaundryRequest> findLaundryALLRequests(String email, String laundryId) {
        Optional<Ban> byId = banRepository.findById(email);
        List<Ban> byBanList = banRepository.findByBanList(email);

        if(byId.isEmpty()){
            return null;
        }

        List<String> emails = Stream.concat(byId.get().getBanList().stream(), byBanList.stream().map(Ban::getEmail)).distinct().collect(Collectors.toList());
        System.out.println(emails);

        return laundryRequestRepository.findByLaundryIdAndMatchedIsFalseAndEmailNotIn(laundryId, emails);

    }

    @Override
    public List<LaundryRequest> findAllMyRequestsByEmail(String email) {
        return laundryRequestRepository.findByEmail(email);
    }

    @Override
    public LaundryRequest saveRequest(LaundryRequestDto laundryRequestDto) {

        Optional<LaundryInfo> byId = laundryInfoRepository.findById(laundryRequestDto.getLaundryId());

        if(byId.isEmpty()){
            return null;
        }
        
        LaundryRequest newData = LaundryRequest.builder()
                .laundryId(laundryRequestDto.getLaundryId())
                .laundryName(byId.get().getName())
                .email(laundryRequestDto.getEmail())
                .gender(laundryRequestDto.getGender())
                .colorTypes(laundryRequestDto.getColorTypes())
                .weight(laundryRequestDto.getWeight())
                .machineTypes(laundryRequestDto.getMachineTypes())
                .extraInfoType(laundryRequestDto.getExtraInfoType())
                .message(laundryRequestDto.getMessage())
                .date(LocalDateTime.now())
                .expireDate(LocalDateTime.now().plusDays(Integer.parseInt(laundryRequestDto.getExpireDay())))
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
    public ResultLaundryRequestDto updateRequest(LaundryRequestDto laundryRequestDto) {
        Optional<LaundryRequest> originalRequest = laundryRequestRepository.findById(laundryRequestDto.get_id());

        if(originalRequest.isEmpty()){
            return ResultLaundryRequestDto.builder().success(false).message("찾는 구인글이 없습니다.").laundryRequest(null).build();
        }

        LaundryRequest origin = originalRequest.get();

        if(origin.isMatched()){
            return ResultLaundryRequestDto.builder().success(false).message("매칭되어있어 수정 불가능합니다.").laundryRequest(null).build();
        }

        LaundryRequest newData = LaundryRequest.builder()
                ._id(origin.get_id())
                .laundryId(origin.getLaundryId())
                .laundryName(origin.getLaundryName())
                .email(origin.getEmail())
                .gender(origin.getGender())
                .colorTypes(laundryRequestDto.getColorTypes())
                .weight(laundryRequestDto.getWeight())
                .machineTypes(laundryRequestDto.getMachineTypes())
                .extraInfoType(laundryRequestDto.getExtraInfoType())
                .message(laundryRequestDto.getMessage())
                .date(origin.getDate())
                .expireDate(origin.getExpireDate())
                .matched(origin.isMatched())
                .version(origin.getVersion())
                .build();

        laundryRequestRepository.save(newData);

        return ResultLaundryRequestDto.builder().success(true).message("성공").laundryRequest(newData).build();
    }

}
