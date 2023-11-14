package com.cau12am.laundryservice.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailCodeRepository extends MongoRepository<EmailCode, String> {
}
