package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.domain.folder.FolderType;
import com.jamsil_team.sugeun.dto.FolderDTO;
import com.jamsil_team.sugeun.dto.DetailFolderDTO;

import java.util.List;

public interface FolderService {
    Folder createFolder(FolderDTO folderDTO);

    void ModifyFolderName(Long folderId, String folderName);

    //TODO 2021.07.13- 작성필요
    //void folderImageModify(FolderDTO folderDTO);

    void removeFolder(Long folderId);

    List<FolderDTO> getListOfFolder(String userId, FolderType type, Long parentFolderId);

     DetailFolderDTO getFolder(String userId, Long folderId);
}
