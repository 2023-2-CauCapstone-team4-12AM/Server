package com.cau12am.laundryservice.domain.Laundry;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LaundryRequestRepository extends MongoRepository<LaundryRequest, String> {
    List<LaundryRequest> findByLaundryId(String laundryId);
    List<LaundryRequest> findByEmail(String email);
}
