package com.cau12am.laundryservice.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedUserMemberRepositoryImpl implements CustomizedUserMemberRepository{
    @Autowired
    private MongoTemplate mongoTemplate;
}
