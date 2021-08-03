package com.jamsil_team.sugeun.domain.folder;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder, Long>, FolderListRepository {

}
