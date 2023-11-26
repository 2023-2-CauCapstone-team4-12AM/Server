package com.cau12am.laundryservice.controller;

import com.cau12am.laundryservice.domain.Match.Match;
import com.cau12am.laundryservice.domain.Result.ResultDto;
import com.cau12am.laundryservice.service.IMatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchController {
    private final IMatchService iMatchService;
    @GetMapping("/doMatch")
    public ResponseEntity<ResultDto> requestMatch(@RequestParam(value = "nowEmail") String nowEmail, @RequestParam(value = "requestId") String requestId, @RequestParam(value = "url") String url){
        log.info("요청글에 대해 매칭하는 컨트롤러 시작");

        ResultDto request = iMatchService.matchRequestByUser(requestId,nowEmail,url);

        if(!request.isSuccess()) {
            return new ResponseEntity<>(request, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @GetMapping("/find/myMatch")
    public ResponseEntity<Map<String, Object>> findMyMatch(@RequestParam(value = "email") String email){

        log.info("매칭 컨트롤 시작");

        List<Match> matches = iMatchService.findMyMatch(email);

        Map<String, Object> result = new HashMap<>();

        result.put("success",true);
        result.put("message","성공");
        result.put("result", matches);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/delete/myMatch")
    public ResponseEntity<ResultDto> deleteMyMatch(@RequestParam(value = "id") String id){
        log.info("매칭 취소 컨트롤 시작");

        ResultDto matches = iMatchService.deleteMatch(id);

        if(!matches.isSuccess()){
            return new ResponseEntity<>(matches, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(matches, HttpStatus.OK);
    }
}
