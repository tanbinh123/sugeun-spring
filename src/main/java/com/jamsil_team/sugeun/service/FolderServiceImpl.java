package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.domain.folder.FolderRepository;
import com.jamsil_team.sugeun.domain.folder.FolderType;
import com.jamsil_team.sugeun.domain.link.LinkRepository;
import com.jamsil_team.sugeun.domain.phrase.PhraseRepository;
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
    private final PhraseRepository phraseRepository;
    private final LinkRepository linkRepository;

    /**
     * 폴더생성
     */
    @Override
    public Folder createFolder(FolderDTO folderDTO) {

        Folder folder = folderDTO.toEntity();

         folderRepository.save(folder);

        return folder;
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


    /**
     * 폴더삭제
     */

    @Override
    public void removeFolder(Long folderId) {

        Folder folder = folderRepository.findById(folderId).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 ID 입니다."));

        if(folder.getType() == FolderType.PHRASE){

            phraseRepository.deleteByFolder(folder);

        }else if (folder.getType() == FolderType.LINK){

            linkRepository.deleteByFolder(folder);

        }

        folderRepository.deleteById(folder.getFolderId());

    }


}
