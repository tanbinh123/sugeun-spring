package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.scheduleSelect.ScheduleSelect;
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
    @Transactional
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
    @Transactional
    @Override
    public void finishUse(Long timeoutId) {
        Timeout timeout = timeoutRepository.findById(timeoutId).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 타임아웃 입니다."));

        //isValid == false 변경
        timeout.isValidFalse();

        //기존 알람 삭제
        timeoutSelectRepository.deleteByTimoutId(timeoutId);

    }

    /**
     * 타임아웃 수정
     */
    @Transactional
    @Override
    public void modifyTimeout(TimeoutDTO timeoutDTO) {

        Timeout timeout = timeoutRepository.findById(timeoutDTO.getTimeoutId()).orElseThrow(() ->
                new IllegalStateException("존재하지 않은 타임아웃입니다."));

        //제목, 유효기간 수정
        timeout.changeTitle(timeoutDTO.getTitle());
        timeout.changeDeadline(timeoutDTO.getDeadline());

        //알람 수정
        //기존 해당 알람 모두 삭제
        timeoutSelectRepository.deleteByTimoutId(timeout.getTimeoutId());

        List<Integer> selected = timeoutDTO.getSelected();

        List<TimeoutSelect> timeoutSelectList = timeoutDTO.getTimeoutSelectEntities(selected, timeout);

        //스케줄 알람 유무 확인
        if(timeoutSelectList != null && timeoutSelectList.size() > 0){
            timeoutSelectList.forEach(scheduleSelect ->
                    timeoutSelectRepository.save(scheduleSelect));
        }



    }

    /**
     * 타임아웃 삭제
     */
    @Transactional
    @Override
    public void removeTimeout(Long timeoutId) {
        //알람 삭제 -> 타임아웃 삭제
        timeoutSelectRepository.deleteByTimoutId(timeoutId);
        timeoutRepository.deleteById(timeoutId);
    }


}
