package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.domain.folder.FolderRepository;
import com.jamsil_team.sugeun.domain.folder.FolderType;
import com.jamsil_team.sugeun.dto.FolderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Transactional
@RequiredArgsConstructor
@Service
public class FolderServiceImpl implements FolderService{

    private final FolderRepository folderRepository;

    /**
     * 폴더생성
     */
    @Override
    public Folder createFolder(FolderDTO folderDTO) {

        Folder folder = folderDTO.toEntity();

        Folder savedFolder = folderRepository.save(folder);

        return savedFolder;
    }

    /**
     * 폴더 이름변경
     */
    @Override
    public void ModifyFolderName(Long folderId, String folderName) {

        Folder folder = folderRepository.findById(folderId).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 ID 입니다."));

        folder.changeFolderName(folderName);
    }

    //TODO 2021.07.10-phrase, link 삭제 구현 후 작성
    /**
     * 폴더삭제
     */
    /*
    @Override
    public void remove(Long folderId) {

        Folder folder = folderRepository.findById(folderId).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 ID 입니다."));

        if(folder.getType() == FolderType.PHRASE){

        }

    }
    */

}
