package com.cau12am.laundryservice.domain.Ban;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BanRepository extends MongoRepository<Ban, String> {
    List<Ban> findByBanList(String email);
}
