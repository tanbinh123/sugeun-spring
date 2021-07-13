package com.jamsil_team.sugeun.domain.link;

import com.jamsil_team.sugeun.domain.folder.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LinkRepository extends JpaRepository<Link, Long> {

    @Modifying(clearAutomatically = true) //쿼리 실행시 JPA 캐싱 clear
    @Query("delete from Link l where l.folder = :folder")
    void deleteByFolder(Folder folder);
}
