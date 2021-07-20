package com.jamsil_team.sugeun.domain.folder;

import com.jamsil_team.sugeun.dto.FolderDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    @Query("select f from Folder f where f.user.userId = :userId")
    List<Folder> getListFolder(String userId);

}
