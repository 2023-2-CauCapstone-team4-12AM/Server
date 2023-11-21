package com.cau12am.laundryservice.controller;

import com.cau12am.laundryservice.domain.Email.EmailCodeDto;
import com.cau12am.laundryservice.domain.Email.EmailPasswordDto;
import com.cau12am.laundryservice.domain.User.UserMemberDto;
import com.cau12am.laundryservice.service.UserMemberService.IUserMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserMemberController {
    private final IUserMemberService userMemberService;

    @GetMapping("/test")
    public String testMethod(){
        return "success";
    }

    @PostMapping("/change-pw")
    public ResponseEntity<Map<String, Object>> changePW(@RequestBody EmailPasswordDto emailPasswordDto){
        log.info("pw 변경 컨드롤러 시작");
        Map<String, Object> result  = userMemberService.changePW(emailPasswordDto.getEmail(), emailPasswordDto.getNowPW(), emailPasswordDto.getNewPW());

        if((boolean) result.get("success") == true){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/send-changedPW")
    public ResponseEntity<Map<String, Object>> sendMailWithPW(@RequestParam(name = "email") @Valid String email){
        log.info("이메일 코드 패스워드 컨드롤러 시작");
        Map<String, Object> result  = userMemberService.sendEmailWithPW(email);

        if((boolean) result.get("success") == true){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/send-email-code")
    public ResponseEntity<Map<String, Object>> sendMailWithCode(@RequestParam(name = "email") @Valid String email){
        log.info("이메일 코드 컨트롤러 시작");
        Map<String, Object> result  = userMemberService.sendEmailWithCode(email);

        if((boolean) result.get("success") == true){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/check-email-code")
    public ResponseEntity<Map<String, Object>> checkMailWithCode(@RequestBody EmailCodeDto emailCodeDto){
        Map<String, Object> result  = userMemberService.checkEmailWithCode(emailCodeDto.getEmail(),emailCodeDto.getCode());
        if((boolean) result.get("success") == true){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, Object>> createAccount(@RequestBody UserMemberDto userMemberDto) {
        Map<String, Object> result = userMemberService.createAccount(userMemberDto);

        if((boolean) result.get("success") == true){
            result.put("messege","성공하였습니다.");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else{
            Map<String, Object> newFailResult = new HashMap<>();
            result.put("messege","실패하였습니다.");
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginAccount(@RequestBody UserMemberDto userMemberDto){
        log.info("로그인 컨트롤러 시작");
        Map<String, Object> result = userMemberService.loginUser(userMemberDto);

        if((boolean) result.get("success") == false) {

            result.put("message","로그인 실패");
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }

        result.put("message","로그인 성공");

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/user-info")
    public ResponseEntity<Map<String, Object>> userInfo(@RequestParam(name = "email") @Valid String email){

        Map<String, Object> result = userMemberService.findUserInfo(email);

        if((boolean) result.get("success") == false){
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteAccount(@RequestBody UserMemberDto userMemberDto) {

        Map<String, Object> result = userMemberService.deleteAccount(userMemberDto.getEmail(), userMemberDto.getPw());

        if((boolean) result.get("success") == false){
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateAccount(@RequestBody UserMemberDto userMemberDto) {

        Map<String, Object> result = userMemberService.updateAccount(userMemberDto);

        if((boolean) result.get("success") == false){
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

}
