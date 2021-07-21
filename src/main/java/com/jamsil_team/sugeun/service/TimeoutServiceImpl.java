package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.timeout.Timeout;
import com.jamsil_team.sugeun.domain.timeout.TimeoutRepository;
import com.jamsil_team.sugeun.domain.timeoutSelect.TimeoutSelect;
import com.jamsil_team.sugeun.domain.timeoutSelect.TimeoutSelectRepository;
import com.jamsil_team.sugeun.dto.TimeoutDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Log4j2
@Transactional
@RequiredArgsConstructor
@Service
public class TimeoutServiceImpl implements TimeoutService{

    private final TimeoutRepository timeoutRepository;
    private final TimeoutSelectRepository timeoutSelectRepository;

    /**
     * 타임아웃 생성
     */
    @Override
    public Timeout createTimeout(TimeoutDTO timeoutDTO) {

        Map<String, Object> entityMap = timeoutDTO.toEntity();

        Timeout timeout = (Timeout) entityMap.get("timeout");

        timeoutRepository.save(timeout);


        List<TimeoutSelect> timeoutSelectList = (List<TimeoutSelect>)entityMap.get("timeoutSelectList");

        if(timeoutSelectList != null && timeoutSelectList.size() > 0){
            timeoutSelectList.forEach(timeoutSelect -> {
                timeoutSelectRepository.save(timeoutSelect);
            });
        }

        return timeout;
    }

    /**
     * 사용완료 기능
     */
    @Override
    public void finishUse(Long timeoutId) {
        Timeout timeout = timeoutRepository.findById(timeoutId).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 타임아웃 입니다."));

        //isValid == false 변경
        timeout.isValidFalse();

        //기존 알람 삭제
        timeoutSelectRepository.deleteByTimoutId(timeoutId);

    }

    @Override
    public void removeTimeout(Long timeoutId) {

    }


}
