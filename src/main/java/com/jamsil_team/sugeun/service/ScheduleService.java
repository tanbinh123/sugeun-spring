package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.schedule.Schedule;
import com.jamsil_team.sugeun.dto.ScheduleDTO;

public interface ScheduleService {

    Schedule createSchedule(ScheduleDTO scheduleDTO);

    void modifySchedule(ScheduleDTO scheduleDTO);

    void removeSchedule(Long scheduleId);

}
