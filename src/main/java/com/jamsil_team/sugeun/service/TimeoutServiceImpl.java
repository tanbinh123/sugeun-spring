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
}
