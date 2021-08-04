package com.jamsil_team.sugeun.domain.timeout;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface TimeoutRepository extends JpaRepository<Timeout, Long> {

    @Query("select t from Timeout t where t.user.userId = :userId order by t.isValid desc ")
    List<Timeout> getTimeoutList(Long userId);


}
