package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.domain.folder.FolderRepository;
import com.jamsil_team.sugeun.domain.folder.FolderType;
import com.jamsil_team.sugeun.domain.link.Link;
import com.jamsil_team.sugeun.domain.link.LinkRepository;
import com.jamsil_team.sugeun.domain.phrase.Phrase;
import com.jamsil_team.sugeun.domain.phrase.PhraseRepository;
import com.jamsil_team.sugeun.dto.folder.FolderDTO;
import com.jamsil_team.sugeun.dto.folder.FolderResDTO;
import com.jamsil_team.sugeun.dto.link.LinkDTO;
import com.jamsil_team.sugeun.dto.phrase.PhraseDTO;
import com.jamsil_team.sugeun.dto.folder.DetailFolderDTO;
import com.jamsil_team.sugeun.file.FileStore;
import com.jamsil_team.sugeun.file.ResultFileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
    private final FileStore fileStore;

    /**
     * 폴더생성
     */
    @Transactional
    @Override
    public Folder createFolder(FolderDTO folderDTO) throws IOException {

        Folder folder = folderDTO.toEntity();

        ResultFileStore resultFileStore = fileStore.storeFile(folderDTO.getImageFile());

        folder.changeFolderImg(resultFileStore);

        folderRepository.save(folder);

        return folder;
    }

    /**
     * 폴더 이름변경
     */
    @Transactional
    @Override
    public void modifyFolderName(Long folderId, String folderName) {

        Folder folder = folderRepository.findById(folderId).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 ID 입니다."));

        folder.changeFolderName(folderName);
    }


    /**
     * 폴더 이미지 변경
     */
    @Transactional
    @Override
    public void modifyFolderImage(Long folderId, MultipartFile multipartFile) throws IOException {

        Folder folder = folderRepository.findById(folderId).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 폴더입니다."));

        //서버 컴퓨터에 저장된 기존 폴더 사진 삭제
        fileRemove(folder);

        ResultFileStore resultFileStore = fileStore.storeFile(multipartFile);

        //사진 저장
        folder.changeFolderImg(resultFileStore);

    }

    /**
     * 폴더삭제
     */
    @Transactional
    @Override
    public void removeFolder(Long folderId) {

        // 글귀,링크 삭제 -> 서버컴퓨터 폴더 사진 삭제 -> 폴더 삭제

       Folder folder = folderRepository.findById(folderId).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 ID 입니다."));

        if(folder.getType() == FolderType.PHRASE){

            phraseRepository.deleteByFolder(folder);

        }else if (folder.getType() == FolderType.LINK){

            linkRepository.deleteByFolder(folder);
        }

        //서버컴퓨터에 있는 폴더 이미지 삭제
        fileRemove(folder);

        folderRepository.deleteById(folder.getFolderId());
    }

    private void fileRemove(Folder folder) {
        //사진 등록이 되어 있는 경우
        if(!folder.getStoreFilename().isBlank()){
            String folderPath = folder.getFolderPath();
            String storeFilename = folder.getStoreFilename();

            File file = new File(fileStore.getFullPath(folderPath, storeFilename));
            file.delete();

//            File thumbnail = new File(fileStore.getThumbnailFullPath(folderPath, "s_" + storeFilename));
//            thumbnail.delete();
        }
    }

    /**
     * 폴더 DTO list
     */
    @Transactional(readOnly = true)
    @Override
    public List<FolderResDTO> getListOfFolder(String userId, FolderType type, Long parentFolderId) {

        List<Folder> result = folderRepository.getListFolder(userId, type, parentFolderId);

        List<FolderResDTO> folderResDTOList = result.stream().map(folder -> {
            FolderResDTO folderResDTO = folder.toResDTO();

            //이미지 파일 데이터
            if(!(folder.getStoreFilename().isBlank())){

                File file = new File(fileStore.getThumbnailFullPath(folder.getFolderPath(), folder.getStoreFilename()));
                byte[] bytes = new byte[0];

                try {
                    bytes = FileCopyUtils.copyToByteArray(file);
                    folderResDTO.setImageData(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return folderResDTO;
        }).collect(Collectors.toList());


        return folderResDTOList;
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

            List<FolderResDTO> folderResDTOList = folderList.stream().map(findFolder -> {
                FolderResDTO folderResDTO = findFolder.toResDTO();
                return folderResDTO;
            }).collect(Collectors.toList());

            //detailFolderDTO 값 추가
            detailFolderDTO.setPhraseDTOList(phraseDTOList);
            detailFolderDTO.setFolderResDTOList(folderResDTOList);

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

            List<FolderResDTO> folderResDTOList = folderList.stream().map(findFolder -> {
                FolderResDTO folderResDTO = findFolder.toResDTO();
                return folderResDTO;
            }).collect(Collectors.toList());

            //detailFolderDTO 값 추가
            detailFolderDTO.setLinkDTOList(linkDTOList);
            detailFolderDTO.setFolderResDTOList(folderResDTOList);

        }

        return detailFolderDTO;
    }
}
