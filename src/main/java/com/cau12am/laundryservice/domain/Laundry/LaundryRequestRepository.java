package com.cau12am.laundryservice.domain.Laundry;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LaundryRequestRepository extends MongoRepository<LaundryRequest, String> {
    List<LaundryRequest> findByLaundryIdAndMatchedIsFalse(String laundryId);
    List<LaundryRequest> findByEmail(String email);

    Optional<LaundryRequest> findOneBy_id(String Id);

}
