package com.cau12am.laundryservice.service;

import com.cau12am.laundryservice.domain.*;
import com.cau12am.laundryservice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserMemberServiceImpl implements IUserMemberService {
    private final UserMemberRepository userMemberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private final EmailService emailService;

    private final EmailCodeRepository emailCodeRepository;


    public Map<String, Object> changePW(String email, String nowPW, String newPW) {
        log.info(" pw 변경 서비스 시작");
        Map<String, Object> result = new HashMap<>();
        UserMember checkMember = userMemberRepository.findOneByEmail(email).orElse(null);

        if(checkMember == null){
            result.put("success", false);
            result.put("message", "메일이 존재 하지 않습니다.");
            return result;
        }

        if(passwordEncoder.matches(nowPW, checkMember.getPw())){
            UserMember newUser = UserMember.builder()
                    ._id(checkMember.get_id())
                    .email(checkMember.getEmail())
                    .pw(passwordEncoder.encode(newPW))
                    .name(checkMember.getName())
                    .refreshToken(checkMember.getRefreshToken())
                    .studentID(checkMember.getStudentID())
                    .sex(checkMember.getSex())
                    .build();

            userMemberRepository.save(newUser);

            result.put("success", true);
            result.put("message", "변경 완료");
            return result;

        } else{
            result.put("success", false);
            result.put("message", "현재 pw가 일치하지 않습니다.");
            return result;
        }
    }

    public Map<String, Object> sendEmailWithPW(String email) {
        log.info(" pw 이메일 전송 서비스 시작");
        Map<String, Object> result = new HashMap<>();
        UserMember checkMember = userMemberRepository.findOneByEmail(email).orElse(null);
        if(checkMember != null){
            String title = "회원 임시 번호";

            SecureRandom secureRandom = new SecureRandom();

            String code = String.valueOf(secureRandom.nextInt(1000000,9999999));

            UserMember newUser = UserMember.builder()
                    ._id(checkMember.get_id())
                    .email(checkMember.getEmail())
                    .pw(passwordEncoder.encode(code))
                    .name(checkMember.getName())
                    .refreshToken(checkMember.getRefreshToken())
                    .studentID(checkMember.getStudentID())
                    .sex(checkMember.getSex())
                    .build();

            userMemberRepository.save(newUser);

            String res = emailService.sendMail(email, title, code);

            result.put("success", true);
            result.put("message", res);
            return result;
        }

        result.put("success", false);
        result.put("message", "메일이 존재 하지 않습니다.");
        return result;
    }

    @Override
    public Map<String, Object> sendEmailWithCode(String email) {
        log.info("인증 이메일 전송 서비스 시작");
        Map<String, Object> result = new HashMap<>();
        UserMember checkMember = userMemberRepository.findOneByEmail(email).orElse(null);
        if(checkMember == null){
            String title = "코인 세탁 어플 이메일 인증 번호";


            SecureRandom secureRandom = new SecureRandom();

            String code = String.valueOf(secureRandom.nextInt(100000,999999));

            emailCodeRepository.save(EmailCode.builder()
                    .email(email)
                    .code(code)
                    .check(false)
                    .build());

            String res = emailService.sendMail(email, title, code);

            result.put("success", true);
            result.put("message", res);
            return result;
        }

        result.put("success", false);
        result.put("message", "가입된 메일이 존재합니다.");
        return result;
    }

    @Override
    public Map<String, Object> checkEmailWithCode(String email, String code) {
        log.info("이메일 인증 번호 확인 시작");
        Map<String, Object> result = new HashMap<>();
        EmailCode findEmailCode = emailCodeRepository.findById(email).orElse(null);
        if(findEmailCode != null){
            if(findEmailCode.getCode().equals(code)){
                emailCodeRepository.save(EmailCode.builder()
                        .email(findEmailCode.getEmail())
                        .code(findEmailCode.getCode())
                        .check(true)
                        .build());

                result.put("success", true);
                result.put("message", "인증 성공");
                return result;
            }
            result.put("success", false);
            result.put("message", "인증 번호 불일치");
            return result;
        }
        result.put("success", false);
        result.put("message", "인증 번호 발급한 적이 없습니다.");
        return result;
    }

    @Override
    public Map<String, Object> createAccount(UserMemberDto userMemberDto) {
        Map<String, Object> data = new HashMap<>();

        EmailCode emailCheck = emailCodeRepository.findById(userMemberDto.getEmail()).orElse(null);

        if(emailCheck == null){
            data.put("success", false);
            return data;
        }

        if(emailCheck.isCheck() == false){
            data.put("success", false);
            return data;
        }

        List<UserMember> existResult = userMemberRepository.findByEmail(userMemberDto.getEmail());

        if(existResult.isEmpty()){

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userMemberDto.getEmail(), passwordEncoder.encode(userMemberDto.getPw()));

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            String accessToken = jwtTokenProvider.createAccessToken(usernamePasswordAuthenticationToken, "user");
            String refreshToken = jwtTokenProvider.createRefreshToken(usernamePasswordAuthenticationToken, "user");

            UserMember newUser = UserMember.builder()
                    .email(userMemberDto.getEmail())
                    .pw(passwordEncoder.encode(userMemberDto.getPw()))
                    .name(userMemberDto.getName())
                    .refreshToken(refreshToken)
                    .studentID(userMemberDto.getStudentID())
                    .sex(userMemberDto.getSex())
                    .build();

            userMemberRepository.save(newUser);
            emailCodeRepository.delete(emailCheck);

            data.put("accessToken", accessToken);
            data.put("refreshToken", refreshToken);
            data.put("success", true);

            return data;
        }

        data.put("success", false);

        return data;
    }

    @Override
    public Map<String, Object> loginUser(UserMemberDto userMemberDto) {

        log.info("로그인 시작");
        UserMember checkMember = userMemberRepository.findOneByEmail(userMemberDto.getEmail()).orElse(null);

        Map<String, Object> resultKey = new HashMap<String, Object>();

        if(checkMember == null){
            log.info("회원 정보가 없습니다.");
            resultKey.put("success", false);
            return null;
        }

        if(!(checkMember.getEmail().equals(userMemberDto.getEmail()) && passwordEncoder.matches(userMemberDto.getPw(),checkMember.getPw()))){
            log.info("로그인 실패");
            resultKey.put("success", false);
            return null;
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(checkMember.getEmail(), checkMember.getPw());

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        String accessToken = jwtTokenProvider.createAccessToken(usernamePasswordAuthenticationToken, "user");
        String refreshToken = jwtTokenProvider.createRefreshToken(usernamePasswordAuthenticationToken, "user");

        UserMember newWithToken = UserMember.builder()
                ._id(checkMember.get_id())
                .sex(checkMember.getSex())
                .email(checkMember.getEmail())
                .pw(checkMember.getPw())
                .studentID(checkMember.getStudentID())
                .name(checkMember.getName())
                .refreshToken(refreshToken)
                .build();

        userMemberRepository.save(newWithToken);

        resultKey.put("accessToken", accessToken);
        resultKey.put("refreshToken", refreshToken);
        resultKey.put("success", true);

        return resultKey;
    }

    @Override
    public Map<String, Object> reCreateAccessToken(String refreshToken) {

        Boolean validationCheck = jwtTokenProvider.validateToken(refreshToken);
        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        UserMember user = userMemberRepository.findOneByEmail(email).orElse(null);

        Map<String, Object> resultKey = new HashMap<String, Object>();

        if(user == null){
            resultKey.put("success", false);
        }


        if(user.getRefreshToken().equals(refreshToken) && validationCheck){
            Authentication authentication = jwtTokenProvider.createAuthentication(email);
            String newAccessToken = jwtTokenProvider.createAccessToken(authentication, "user");
            String newRefreshToken = jwtTokenProvider.createRefreshToken(authentication, "user");

            UserMember newWithToken = UserMember.builder()
                    ._id(user.get_id())
                    .sex(user.getSex())
                    .email(user.getEmail())
                    .pw(user.getPw())
                    .studentID(user.getStudentID())
                    .name(user.getName())
                    .refreshToken(newRefreshToken)
                    .build();

            userMemberRepository.save(newWithToken);

            resultKey.put("accessToken", newAccessToken);
            resultKey.put("refreshToken", newRefreshToken);
            resultKey.put("success", true);
            return resultKey;
        }

        resultKey.put("success", false);
        return resultKey;
    }

    @Override
    public Map<String, Object> logout(String refreshToken) {

        log.info("로그 아웃 서비스 시작");

        UserMember user = userMemberRepository.findOneByRefreshToken(refreshToken).orElse(null);
        Map<String, Object> result = new HashMap<>();

        System.out.println(user);

        if(user != null){
            UserMember removeTokenUser = UserMember.builder()
                    ._id(user.get_id())
                    .sex(user.getSex())
                    .email(user.getEmail())
                    .pw(user.getPw())
                    .studentID(user.getStudentID())
                    .name(user.getName())
                    .refreshToken(null)
                    .build();
            userMemberRepository.save(removeTokenUser);
            result.put("success",true);
            return result;
        }
        result.put("success",false);
        return result;
    }

    @Override
    public Map<String, Object> findUserInfo(String email) {
        UserMember userMember = userMemberRepository.findOneByEmail(email).orElse(null);
        Map<String, Object> result = new HashMap<>();
        if(userMember == null){
            result.put("success", false);
            result.put("message", "이메일을 잘못입력하셨습니다.");
        } else{
            UserMember info = UserMember.builder().sex(userMember.getSex()).name(userMember.getName()).studentID(userMember.getStudentID()).email(userMember.getEmail()).build();
            result.put("user", info);
            result.put("success", true);
            result.put("message", "조회 완료");
        }
        return result;
    }


    @Override
    public Map<String, Object> deleteAccount(String email, String pw) {

        UserMember userMember = userMemberRepository.findOneByEmail(email).orElse(null);
        Map<String, Object> result = new HashMap<>();
        if(userMember == null){
            result.put("success", false);
            result.put("message", "이메일을 잘못입력하셨습니다.");
        } else{
            if(passwordEncoder.matches(pw, userMember.getPw())){
                userMemberRepository.delete(userMember);
                result.put("success", true);
                result.put("message", "삭제 완료");
            } else{
                result.put("success", false);
                result.put("message", "비밀번호를 잘못입력하셨습니다.");
            }
        }
        return result;
    }

    @Override
    public Map<String, Object> updateAccount(UserMemberDto userMemberDto) {
        UserMember userMember = userMemberRepository.findOneByEmail(userMemberDto.getEmail()).orElse(null);
        Map<String, Object> result = new HashMap<>();
        if(userMember == null){
            result.put("success", false);
            result.put("message", "이메일을 잘못입력하셨습니다.");
        } else{
            if(passwordEncoder.matches(userMemberDto.getPw(), userMember.getPw())){
                UserMember newUser = UserMember.builder()
                        ._id(userMember.get_id())
                        .email(userMember.getEmail())
                        .pw(userMember.getPw())
                        .name(userMemberDto.getName())
                        .studentID(userMemberDto.getStudentID())
                        .refreshToken(userMember.getRefreshToken())
                        .sex(userMember.getSex())
                        .build();
                userMemberRepository.save(newUser);
                result.put("success", true);
                result.put("message", "수정 완료");
            } else{
                result.put("success", false);
                result.put("message", "비밀번호를 잘못입력하셨습니다.");
            }
        }
        return result;
    }
}
