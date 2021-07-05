package com.jamsil_team.sugeun.domain.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findByUserId(String userId);

    @Query("select u.deviceToken from User u where u.deviceToken = :userId")
    String deviceTokenFindByUserId(@Param("userId") String userId);
}
