package com.jamsil_team.sugeun.domain.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("select s from Schedule s where s.user.userId = :userId")
    List<Schedule> getScheduleList(Long userId);


}
