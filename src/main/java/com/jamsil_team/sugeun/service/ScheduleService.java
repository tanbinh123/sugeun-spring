package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.schedule.Schedule;
import com.jamsil_team.sugeun.dto.schedule.ScheduleDTO;

import java.util.List;

public interface ScheduleService {

    Schedule createSchedule(ScheduleDTO scheduleDTO);

    void modifySchedule(ScheduleDTO scheduleDTO);

    void removeSchedule(Long scheduleId);

    List<ScheduleDTO> getListOfSchedule(Long userId);

}
