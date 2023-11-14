package com.cau12am.laundryservice.security;

import com.cau12am.laundryservice.domain.UserMember;
import com.cau12am.laundryservice.domain.UserMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserMemberRepository userMemberRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserMember userMember= userMemberRepository.findOneByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾지 못함"));
        return User.builder()
                .password(userMember.getPw())
                .username(userMember.getEmail())
                .authorities("user")
                .build();
    }
}
