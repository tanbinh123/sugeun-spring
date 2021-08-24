package com.jamsil_team.sugeun.service.schedule;

import com.jamsil_team.sugeun.domain.schedule.Schedule;
import com.jamsil_team.sugeun.dto.schedule.ScheduleDTO;
import com.jamsil_team.sugeun.dto.schedule.ScheduleResDTO;

import java.util.List;

public interface ScheduleService {

    Schedule createSchedule(ScheduleDTO scheduleDTO);

    void modifySchedule(ScheduleDTO scheduleDTO);

    void removeSchedule(Long scheduleId);

    List<ScheduleResDTO> getListOfSchedule(Long userId);

}
