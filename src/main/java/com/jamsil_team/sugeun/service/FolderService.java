package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.domain.folder.FolderType;
import com.jamsil_team.sugeun.dto.folder.FolderDTO;
import com.jamsil_team.sugeun.dto.folder.DetailFolderDTO;
import com.jamsil_team.sugeun.dto.folder.FolderResDTO;

import java.io.IOException;
import java.util.List;

public interface FolderService {
    Folder createFolder(FolderDTO folderDTO) throws IOException;

    void modifyFolderName(Long folderId, String folderName);

    //TODO 2021.07.13- 작성필요
    //void modifyFolderImage(FolderDTO folderDTO);

    void removeFolder(Long folderId);

    List<FolderResDTO> getListOfFolder(String userId, FolderType type, Long parentFolderId);

     DetailFolderDTO getFolder(String userId, Long folderId);
}
