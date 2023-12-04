package com.cau12am.laundryservice;

import com.cau12am.laundryservice.service.MatchServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MatchingRepositoryTest {

    @Autowired
    MatchServiceImpl matchService;

    @Test
    @DisplayName("멤버 만들기")
    void createMember() {


        /*
        when
         */
        matchService.matchRequestByUser("656477c7440c342fea748d65", "wjdtjdduq","t1");
        matchService.matchRequestByUser("656477c7440c342fea748d65", "wjdtjdduqasdf","asdfsadf");
    }
}