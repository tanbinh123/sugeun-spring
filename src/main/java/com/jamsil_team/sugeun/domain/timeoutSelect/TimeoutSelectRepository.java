package com.jamsil_team.sugeun.domain.timeoutSelect;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TimeoutSelectRepository extends JpaRepository<TimeoutSelect, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true) //쿼리 실행시 JPA 캐싱 clear
    @Query("delete from TimeoutSelect ts where ts.timeout.timeoutId = :timeoutId")
    void deleteByTimoutId(Long timeoutId);

    @Query("select ts.selected from TimeoutSelect ts where ts.timeout.timeoutId = :timeoutId")
    List<Integer> selectedByTimeoutId(Long timeoutId);

    //테스트코드 사용
    @Query("select ts from TimeoutSelect ts " +
            "left join Timeout t on t.timeoutId = ts.timeout.timeoutId " +
            "where t.user.userId = :userId")
    List<TimeoutSelect> findByUserId(Long userId);

    //테스트코드 사용
    @Query("select ts from TimeoutSelect ts where ts.timeout.timeoutId = :timeoutId")
    List<TimeoutSelect> findByTimeoutId(Long timeoutId);

    //테스트코드 사용
    @Query("select count(ts.timeoutSelectId) from TimeoutSelect ts where ts.timeout.timeoutId = :timeoutId")
    Long alarmCountByTimeoutId(Long timeoutId);

}
