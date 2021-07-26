package com.jamsil_team.sugeun.domain.scheduleSelect;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduleSelectRepository extends JpaRepository<ScheduleSelect, Long> {

    @Modifying(clearAutomatically = true) //쿼리 실행시 JPA 캐싱 clear
    @Query("delete from ScheduleSelect ss where ss.schedule.scheduleId = :scheduleId")
    void deleteByScheduleId(Long scheduleId);


    //테스트코드에 사용
    @Query("select ss from ScheduleSelect ss " +
            "left join Schedule s on s = ss.schedule " +
            "where s.user.userId = :userId")
    List<ScheduleSelect> findByUserId(String userId);

    //테스트코드에 사용
    @Query("select ss from ScheduleSelect ss where ss.schedule.scheduleId = :scheduleId")
    List<ScheduleSelect> findByScheduleId(Long scheduleId);


}
