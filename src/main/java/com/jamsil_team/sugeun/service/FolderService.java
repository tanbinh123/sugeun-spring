package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.dto.FolderDTO;

public interface FolderService {
    Folder createFolder(FolderDTO folderDTO);

    void folderNameModify(Long folderId, String folderName);

    //void folderImageModify(FolderDTO folderDTO);

    //void remove(Long folderId);
}
