package com.cau12am.laundryservice.service;

import com.cau12am.laundryservice.domain.Match.Match;
import com.cau12am.laundryservice.domain.Result.ResultDto;

import java.util.List;

public interface IMatchService {
    ResultDto matchRequestByUser(String requestId, String nowEmail, String url);
    List<Match> findMyMatch(String email);

    ResultDto deleteMatch(String Id);
}
