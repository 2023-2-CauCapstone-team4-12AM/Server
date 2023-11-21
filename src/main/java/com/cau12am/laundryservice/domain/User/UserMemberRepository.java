package com.cau12am.laundryservice.domain.User;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserMemberRepository extends MongoRepository<UserMember, String>, CustomizedUserMemberRepository {
    List<UserMember> findByEmail(String email);
    Optional<UserMember> findOneByEmail(String email);
}
