package com.jamsil_team.sugeun.domain.folder;

import com.jamsil_team.sugeun.dto.FolderDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long>, FolderListRepository {

}
