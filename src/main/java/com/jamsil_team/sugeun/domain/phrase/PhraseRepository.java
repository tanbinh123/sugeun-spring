package com.jamsil_team.sugeun.domain.phrase;

import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.dto.PhraseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhraseRepository extends JpaRepository<Phrase, Long> {

    @Modifying(clearAutomatically = true) //쿼리 실행시 JPA 캐싱 clear
    @Query("delete from Phrase p where p.folder = :folder")
    void deleteByFolder(Folder folder);

    //글귀 리스트
    @Query("select p from Phrase p where p.user.userId = :userId " +
            "and p.folder.folderId = :folderId")
    List<Phrase> getPhraseList(@Param("userId") String userId, @Param("folderId") Long folderId);

    //북마크
    @Query("select p from Phrase p where p.user.userId = :userId and " +
            "p.bookmark = true")
    List<Phrase> getPhraseBookmarkList(String userId);


}
