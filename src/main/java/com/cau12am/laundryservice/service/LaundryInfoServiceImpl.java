package com.cau12am.laundryservice.service;

import com.cau12am.laundryservice.domain.Laundry.LaundryInfo;
import com.cau12am.laundryservice.domain.Laundry.LaundryInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LaundryInfoServiceImpl implements ILaundryInfoService{
    private final LaundryInfoRepository laundryInfoRepository;
    @Override
    public List<LaundryInfo> findLaundry(String lon, String lat, String dis) {
        log.info("근처 코인세탁소 찾는 서비스 시작");

        Point point = new Point(Double.parseDouble(lon), Double.parseDouble(lat));
        Distance distance = new Distance(Double.parseDouble(dis), Metrics.KILOMETERS);

        List<LaundryInfo> nearLaundries = laundryInfoRepository.findByLocationNear( point, distance);

        return nearLaundries;
    }
}
