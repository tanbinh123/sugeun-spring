package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.domain.folder.FolderRepository;
import com.jamsil_team.sugeun.domain.folder.FolderType;
import com.jamsil_team.sugeun.domain.link.Link;
import com.jamsil_team.sugeun.domain.link.LinkRepository;
import com.jamsil_team.sugeun.domain.phrase.Phrase;
import com.jamsil_team.sugeun.domain.phrase.PhraseRepository;
import com.jamsil_team.sugeun.dto.FolderDTO;
import com.jamsil_team.sugeun.dto.LinkDTO;
import com.jamsil_team.sugeun.dto.PhraseDTO;
import com.jamsil_team.sugeun.dto.DetailFolderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    @Transactional
    @Override
    public Folder createFolder(FolderDTO folderDTO) {

        Folder folder = folderDTO.toEntity();

        System.out.println("====================");
        System.out.println(folder.getType());
        System.out.println(folder.getType().equals(FolderType.PHRASE));
        System.out.println(folder);

        System.out.println(folder.getUser());
         folderRepository.save(folder);

        return folder;
    }

    /**
     * 폴더 이름변경
     */
    @Transactional
    @Override
    public void ModifyFolderName(Long folderId, String folderName) {

        Folder folder = folderRepository.findById(folderId).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 ID 입니다."));

        folder.changeFolderName(folderName);
    }


    /**
     * 폴더삭제
     */
    @Transactional
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
    /**
     * 폴더 DTO list
     */
    @Transactional(readOnly = true)
    @Override
    public List<FolderDTO> getListOfFolder(String userId, FolderType type, Long parentFolderId) {

        List<Folder> result = folderRepository.getListFolder(userId, type, parentFolderId);

        List<FolderDTO> folderDTOList = result.stream().map(folder -> {
            FolderDTO folderDTO = folder.toDTO();
            return folderDTO;
        }).collect(Collectors.toList());


        return folderDTOList;
    }


    /**
     * 폴더 상세조회
     */
    @Transactional(readOnly = true)
    @Override
    public DetailFolderDTO getFolder(String userId, Long folderId) {

        Folder folder = folderRepository.findById(folderId).orElseThrow(() ->
                new IllegalStateException("존재하지 않은 폴더입니다."));

        //빈 detailFOlderDTO 생성
        DetailFolderDTO detailFolderDTO = new DetailFolderDTO();

        //글귀 폴더일 경우
        if(folder.getType() == FolderType.PHRASE){

            //글귀 리스트
            List<Phrase> phraseList = phraseRepository.getPhraseList(userId, folderId);

            List<PhraseDTO> phraseDTOList = phraseList.stream().map(findPhrase -> {
                PhraseDTO phraseDTO = findPhrase.toDTO();
                return phraseDTO;
            }).collect(Collectors.toList());

            //폴더 리스트
            List<Folder> folderList = folderRepository.getListFolder(userId, folder.getType(), folderId);

            List<FolderDTO> folderDTOList = folderList.stream().map(findFolder -> {
                FolderDTO folderDTO = findFolder.toDTO();
                return folderDTO;
            }).collect(Collectors.toList());

            //detailFolderDTO 값 추가
            detailFolderDTO.setPhraseDTOList(phraseDTOList);
            detailFolderDTO.setFolderDTOList(folderDTOList);

        }
        //링크 폴더일 경우
        else if(folder.getType() == FolderType.LINK){

            //링크 리스트
            List<Link> linkList = linkRepository.getLinkList(userId, folderId);

            List<LinkDTO> linkDTOList = linkList.stream().map(findLink -> {
                LinkDTO linkDTO = findLink.toDTO();
                return linkDTO;
            }).collect(Collectors.toList());

            //폴더 리스트
            List<Folder> folderList = folderRepository.getListFolder(userId, folder.getType(), folderId);

            List<FolderDTO> folderDTOList = folderList.stream().map(findFolder -> {
                FolderDTO folderDTO = findFolder.toDTO();
                return folderDTO;
            }).collect(Collectors.toList());

            //detailFolderDTO 값 추가
            detailFolderDTO.setLinkDTOList(linkDTOList);
            detailFolderDTO.setFolderDTOList(folderDTOList);

        }

        return detailFolderDTO;
    }
}
