package com.cau12am.laundryservice.service;

import com.cau12am.laundryservice.domain.Ban.Ban;
import com.cau12am.laundryservice.domain.Ban.BanRepository;
import com.cau12am.laundryservice.domain.Result.ResultDto;
import com.cau12am.laundryservice.domain.User.UserMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class BanServiceImpl {
    private final BanRepository banRepository;

    public ResultDto insertBanList(String myEmail, String targetEmail){
        Optional<Ban> oneByEmail = banRepository.findById(myEmail);
        if(oneByEmail.isEmpty()){
            return ResultDto.builder().success(false).message("존재하지 않는 계정입니다.").build();
        }
        Ban ban = oneByEmail.get();

        List<String> banList = ban.getBanList();
        boolean contains = banList.contains(targetEmail);

        if(!contains){
            banList.add(targetEmail);
        }

        banRepository.save(ban);

        return ResultDto.builder().success(true).message("추가 성공하였습니다.").build();
    }

    public ResultDto deleteBanList(String myEmail, String targetEmail){
        Optional<Ban> oneByEmail = banRepository.findById(myEmail);
        if(oneByEmail.isEmpty()){
            return ResultDto.builder().success(false).message("존재하지 않는 계정입니다.").build();
        }
        Ban ban = oneByEmail.get();

        List<String> banList = ban.getBanList();
        boolean contains = banList.contains(targetEmail);

        if(contains){
            banList.remove(targetEmail);
        }

        banRepository.save(ban);

        return ResultDto.builder().success(true).message("삭제 성공하였습니다.").build();
    }
}
