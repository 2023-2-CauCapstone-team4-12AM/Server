package com.cau12am.laundryservice.domain.Notification;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FCMTokenRepository extends MongoRepository<FCMToken, String> {
}
