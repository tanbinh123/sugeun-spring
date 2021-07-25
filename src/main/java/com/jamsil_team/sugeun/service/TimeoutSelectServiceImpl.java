package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.timeout.Timeout;
import com.jamsil_team.sugeun.domain.timeout.TimeoutRepository;
import com.jamsil_team.sugeun.domain.timeoutSelect.TimeoutSelect;
import com.jamsil_team.sugeun.domain.timeoutSelect.TimeoutSelectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class TimeoutSelectServiceImpl implements TimeoutSelectService{

    private final TimeoutSelectRepository timeoutSelectRepository;
    private final TimeoutRepository timeoutRepository;

    /**
     * 알람변경
     */
    @Transactional
    @Override
    public void modifyAlarm(Long timeoutId, List<Integer> selected) {

        Timeout timeout = timeoutRepository.findById(timeoutId).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 타임아웃 입니다."));

        // 기존 알람 모두 삭제
        timeoutSelectRepository.deleteByTimoutId(timeoutId);

        //알람선택 유무 확인
        if( selected != null && selected.size() > 0){
            //알림 추가
            List<TimeoutSelect> timeoutSelectList = selected.stream().map(integer -> {
                TimeoutSelect timeoutSelect = TimeoutSelect.builder()
                        .timeout(timeout)
                        .selected(integer)
                        .alarmDateTime(timeout.getDeadline()
                                .minusDays(integer.longValue()).minusHours(11).minusMinutes(59))
                        .build();
                return timeoutSelect;
            }).collect(Collectors.toList());

            //저장
            timeoutSelectList.forEach(timeoutSelect -> {
                timeoutSelectRepository.save(timeoutSelect); });

        }


    }
}
