package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.dto.FolderDTO;

public interface FolderService {
    Folder createFolder(FolderDTO folderDTO);

    void ModifyFolderName(Long folderId, String folderName);

    //TODO 2021.07.13- 작성필요
    //void folderImageModify(FolderDTO folderDTO);

    //void remove(Long folderId);
}
