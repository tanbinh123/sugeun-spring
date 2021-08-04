package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.domain.folder.FolderType;
import com.jamsil_team.sugeun.dto.folder.FolderDTO;
import com.jamsil_team.sugeun.dto.folder.DetailFolderDTO;
import com.jamsil_team.sugeun.dto.folder.FolderResDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FolderService {
    Folder createFolder(FolderDTO folderDTO) throws IOException;

    void modifyFolderName(Long folderId, String folderName);

    void modifyFolderImage(Long folderId, MultipartFile multipartFile) throws IOException;

    void removeFolder(Long folderId);

    List<FolderResDTO> getListOfFolder(String userId, FolderType type, Long parentFolderId);

    DetailFolderDTO getFolder(String userId, Long folderId);
}
