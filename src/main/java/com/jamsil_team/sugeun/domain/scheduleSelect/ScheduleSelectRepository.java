package com.jamsil_team.sugeun.domain.scheduleSelect;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduleSelectRepository extends JpaRepository<ScheduleSelect, Long> {

    //테스트코드에 사용
    @Query("select ss from ScheduleSelect ss " +
            "left join Schedule s on s = ss.schedule " +
            "where s.user.userId = :userId")
    List<ScheduleSelect> findByUserId(String userId);


}
