package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.schedule.Schedule;
import com.jamsil_team.sugeun.domain.schedule.ScheduleRepository;
import com.jamsil_team.sugeun.domain.scheduleSelect.ScheduleSelect;
import com.jamsil_team.sugeun.domain.scheduleSelect.ScheduleSelectRepository;
import com.jamsil_team.sugeun.dto.ScheduleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;
    private final ScheduleSelectRepository scheduleSelectRepository;

    /**
     * 스케줄 생성
     */
    @Override
    public Schedule createSchedule(ScheduleDTO scheduleDTO) {

        Map<String, Object> entityMap = scheduleDTO.toEntity();

        //schedule 저장
        Schedule schedule = (Schedule) entityMap.get("schedule");

        scheduleRepository.save(schedule);

        //scheduleSelect 알람 저장
        List<ScheduleSelect> scheduleSelectList = (List<ScheduleSelect>) entityMap.get("scheduleSelectList");

        if(scheduleSelectList != null && scheduleSelectList.size() > 0){
            scheduleSelectList.forEach(scheduleSelect ->
                    scheduleSelectRepository.save(scheduleSelect));
        }

        return schedule;
    }
}
