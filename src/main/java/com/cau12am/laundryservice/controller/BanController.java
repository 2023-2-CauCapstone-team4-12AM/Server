package com.cau12am.laundryservice.controller;

import com.cau12am.laundryservice.domain.Result.ResultDto;
import com.cau12am.laundryservice.service.BanServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ban")
public class BanController {
    private final BanServiceImpl banService;
    @GetMapping("/insert")
    public ResponseEntity<ResultDto> insertBanList(@RequestParam(name = "myEmail") String myEmail, @RequestParam(name = "targetEmail") String targetEmail){
        ResultDto result = banService.insertBanList(myEmail, targetEmail);

        if(!result.isSuccess()){
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/delete")
    public ResponseEntity<ResultDto> deleteBanList(@RequestParam(name = "myEmail") String myEmail, @RequestParam(name = "targetEmail") String targetEmail){
        ResultDto result = banService.deleteBanList(myEmail, targetEmail);

        if(!result.isSuccess()){
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
