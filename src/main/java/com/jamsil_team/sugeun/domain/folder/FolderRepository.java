package com.jamsil_team.sugeun.domain.folder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long>, FolderListRepository {

    @Query("select f from Folder f where f.parentFolder.folderId is null and f.user.userId = :userId")
    List<Folder> topFolderList(Long userId);

    @Query("select f from Folder f where f.parentFolder.folderId = :parentFolderId")
    List<Folder> childFolderList(Long parentFolderId);

}
