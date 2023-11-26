package com.cau12am.laundryservice.service;

import com.cau12am.laundryservice.domain.Laundry.LaundryRequest;
import com.cau12am.laundryservice.domain.Laundry.LaundryRequestRepository;
import com.cau12am.laundryservice.domain.Match.Match;
import com.cau12am.laundryservice.domain.Match.MatchRepository;
import com.cau12am.laundryservice.domain.Result.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MatchServiceImpl implements IMatchService{

    private final LaundryRequestRepository laundryRequestRepository;
    private final MatchRepository matchRepository;
    @Override
    public ResultDto matchRequestByUser(String requestId, String nowEmail, String url) {
        try{
            Optional<LaundryRequest> oneById = laundryRequestRepository.findOneBy_id(requestId);

            if(oneById.isEmpty()){
                return ResultDto.builder().success(false).message("구인글을 찾을수없습니다.").build();
            }

            if(oneById.get().isMatched()){
                return ResultDto.builder().success(false).message("이미 매칭 완료된 요청글입니다.").build();
            }

            LaundryRequest laundryRequest = oneById.get();

            laundryRequestRepository.save(
                    LaundryRequest.builder()
                            ._id(laundryRequest.get_id())
                            .laundryId(laundryRequest.getLaundryId())
                            .email(laundryRequest.getEmail())
                            .gender(laundryRequest.getGender())
                            .colorTypes(laundryRequest.getColorTypes())
                            .weight(laundryRequest.getWeight())
                            .machineTypes(laundryRequest.getMachineTypes())
                            .extraInfoType(laundryRequest.getExtraInfoType())
                            .message(laundryRequest.getMessage())
                            .date(laundryRequest.getDate())
                            .matched(true)
                            .version(laundryRequest.getVersion())
                            .build()
            );

            ArrayList<String> userEmails = new ArrayList<>();
            userEmails.add(oneById.get().getEmail());
            userEmails.add(nowEmail);

            Match newMatchInfo = Match.builder()
                    .requestId(requestId)
                    .users(userEmails)
                    .url(url)
                    .date(new Date())
                    .build();

            matchRepository.save(newMatchInfo);

            return ResultDto.builder().success(true).message("매칭 성공").build();
        } catch (OptimisticLockingFailureException e){
            return ResultDto.builder().success(false).message("타인이 먼저 매칭하였습니다.").build();
        }
    }

    @Override
    public List<Match> findMyMatch(String email) {
        return matchRepository.findByUsers(email);
    }

    @Override
    public ResultDto deleteMatch(String Id) {

        Optional<Match> findMatch = matchRepository.findById(Id);

        if(findMatch.isEmpty()){
            return ResultDto.builder().success(false).message("매칭이 존재하지 않습니다.").build();
        }

        Optional<LaundryRequest> findRequest = laundryRequestRepository.findById(findMatch.get().getRequestId());

        if(findRequest.isEmpty()){
            return ResultDto.builder().success(false).message("요청글이 존재하지 않습니다.").build();
        }

        matchRepository.delete(findMatch.get());

        LaundryRequest laundryRequest = findRequest.get();

        laundryRequestRepository.save(
                LaundryRequest.builder()
                        ._id(laundryRequest.get_id())
                        .laundryId(laundryRequest.getLaundryId())
                        .email(laundryRequest.getEmail())
                        .gender(laundryRequest.getGender())
                        .colorTypes(laundryRequest.getColorTypes())
                        .weight(laundryRequest.getWeight())
                        .machineTypes(laundryRequest.getMachineTypes())
                        .extraInfoType(laundryRequest.getExtraInfoType())
                        .message(laundryRequest.getMessage())
                        .date(laundryRequest.getDate())
                        .matched(false)
                        .version(laundryRequest.getVersion())
                        .build()
        );

        return ResultDto.builder().success(true).message("매칭이 취소되었습니다.").build();
    }
}
