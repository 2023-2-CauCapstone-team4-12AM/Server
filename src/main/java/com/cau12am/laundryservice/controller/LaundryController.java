package com.cau12am.laundryservice.controller;

import com.cau12am.laundryservice.domain.Laundry.LaundryInfo;
import com.cau12am.laundryservice.domain.Laundry.LaundryRequest;
import com.cau12am.laundryservice.domain.Laundry.LaundryRequestDto;
import com.cau12am.laundryservice.domain.Laundry.UserPlaceDto;
import com.cau12am.laundryservice.domain.Result.ResultLaundryInfoDto;
import com.cau12am.laundryservice.service.ILaundryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/laundry")
public class LaundryController {

    private final ILaundryService iLaundryService;
    @PostMapping("/findNear")
    public ResponseEntity<ResultLaundryInfoDto> findNearLaundry(@Valid @RequestBody UserPlaceDto userPlaceDto){

        log.info("근처 코인세탁소 찾는 컨트롤러 시작");

        List<LaundryInfo> laundries = iLaundryService.findLaundry(
                userPlaceDto.getLon(), userPlaceDto.getLat(), userPlaceDto.getDistance());

        ResultLaundryInfoDto result = ResultLaundryInfoDto.builder()
                .success(true)
                .message("성공")
                .laundryInfoList(laundries)
                .build();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/request/allInLaundry")
    public ResponseEntity<Map<String, Object>> findLaundryRequest(@RequestParam(name = "laundryId") @Valid String laundryId){

        log.info("특정 코인세탁소 요청글 찾는 컨트롤러 시작");

        List<LaundryRequest> requests = iLaundryService.findLaundryALLRequests(laundryId);

        Map<String, Object> result = new HashMap<>();
        result.put("success",true);
        result.put("message","성공");
        result.put("result",requests);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PostMapping("/myRequest/save")
    public ResponseEntity<Map<String, Object>> saveMyRequest(@RequestBody LaundryRequestDto laundryRequestDto){
        log.info("자신의 요청글 작성 컨트롤러 시작");

        System.out.println(laundryRequestDto);

        LaundryRequest saveRequest = iLaundryService.saveRequest(laundryRequestDto);

        Map<String, Object> result = new HashMap<>();

        result.put("success",true);
        result.put("message","성공");
        result.put("result", saveRequest);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/myRequest/update")
    public ResponseEntity<Map<String, Object>> updateMyRequest(@RequestBody LaundryRequestDto laundryRequestDto){
        log.info("자신이 쓴 요청글 수정 컨트롤러 시작");

        Optional<LaundryRequest> saveRequest = iLaundryService.updateRequest(laundryRequestDto);

        Map<String, Object> result = new HashMap<>();

        if(saveRequest.isEmpty()) {
            result.put("success",false);
            result.put("message","업데이트를 원하는 요청글이 찾을 수 없습니다.");
            result.put("result", saveRequest.get());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        result.put("success",true);
        result.put("message","성공");
        result.put("result", saveRequest.get());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/myRequest/delete")
    public ResponseEntity<Map<String, Object>> deleteMyRequest(@RequestHeader (value = "Id") String Id){
        log.info("자신이 쓴 요청글 삭제 컨트롤러 시작");

        iLaundryService.deleteRequest(Id);

        Map<String, Object> result = new HashMap<>();

        result.put("success",true);
        result.put("message","삭제 성공");

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/request/byId")
    public ResponseEntity<Map<String, Object>> findRequestById(@RequestHeader (value = "Id") String Id){
        log.info("글의 아이디로 요청글 찾는 컨트롤러 시작");

        Optional<LaundryRequest> request = iLaundryService.findRequestById(Id);

        Map<String, Object> result = new HashMap<>();

        if(request.isEmpty()) {
            result.put("success",false);
            result.put("message","업데이트를 원하는 요청글이 찾을 수 없습니다.");
            result.put("result", request.get());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        result.put("success",true);
        result.put("message","성공");
        result.put("result", request.get());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/request/byEmail")
    public ResponseEntity<Map<String, Object>> findRequestsByEmail(@RequestHeader(value = "email") String email){
        log.info("글의 아이디로 요청글 찾는 컨트롤러 시작");

        List<LaundryRequest> requests = iLaundryService.findAllMyRequestsByEmail(email);

        Map<String, Object> result = new HashMap<>();

        result.put("success",true);
        result.put("message","성공");
        result.put("result", requests);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
