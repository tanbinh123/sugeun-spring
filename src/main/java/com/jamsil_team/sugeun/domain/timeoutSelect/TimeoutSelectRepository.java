package com.jamsil_team.sugeun.domain.timeoutSelect;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TimeoutSelectRepository extends JpaRepository<TimeoutSelect, Long> {

    @Query("select ts from TimeoutSelect ts " +
            "left join Timeout t on t.timeoutId = ts.timeout.timeoutId " +
            "where t.user.userId = :userId")
    List<TimeoutSelect> findByUserId(String userId);
}
