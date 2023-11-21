package com.cau12am.laundryservice.domain.Laundry;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LaundryInfoRepository extends MongoRepository<LaundryInfo, String>{
    List<LaundryInfo> findByLocationNear(Point location, Distance maxDistance);
}
