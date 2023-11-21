package com.cau12am.laundryservice.service.UserMemberService;


import com.cau12am.laundryservice.domain.User.UserMemberDto;

import java.util.Map;

public interface IUserMemberService {

    public Map<String, Object> changePW(String email, String nowPW, String newPW);

    public Map<String, Object> sendEmailWithPW(String email);

    public Map<String, Object> sendEmailWithCode(String email);

    public Map<String, Object> checkEmailWithCode(String email, String code);

    public Map<String, Object> createAccount(UserMemberDto userMemberDto);

    public Map<String, Object> loginUser(UserMemberDto userMemberDto);

    public Map<String, Object> findUserInfo(String email);

    public Map<String, Object> deleteAccount(String email, String pw);

    public Map<String, Object> updateAccount(UserMemberDto userMemberDto);

}
