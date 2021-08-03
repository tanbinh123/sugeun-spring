package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.domain.folder.FolderRepository;
import com.jamsil_team.sugeun.domain.folder.FolderType;
import com.jamsil_team.sugeun.domain.link.Link;
import com.jamsil_team.sugeun.domain.link.LinkRepository;
import com.jamsil_team.sugeun.domain.phrase.Phrase;
import com.jamsil_team.sugeun.domain.phrase.PhraseRepository;
import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.domain.user.UserRepository;
import com.jamsil_team.sugeun.dto.folder.DetailFolderDTO;
import com.jamsil_team.sugeun.dto.folder.FolderDTO;
import com.jamsil_team.sugeun.dto.folder.FolderResDTO;
import com.jamsil_team.sugeun.dto.link.LinkDTO;
import com.jamsil_team.sugeun.dto.phrase.PhraseDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class FolderServiceImplTest {

    @Autowired FolderService folderService;
    @Autowired UserRepository userRepository;
    @Autowired FolderRepository folderRepository;
    @Autowired PhraseRepository phraseRepository;
    @Autowired LinkRepository linkRepository;

    @Test
    void 폴더생성_이미지o() throws Exception{
        //given
        User user = createUser();

        MockMultipartFile file = new MockMultipartFile("file", "filename-1.jpeg", "image/jpeg", "some-image".getBytes());

        FolderDTO folderDTO = FolderDTO.builder()
                .folderName("파일A")
                .userId(user.getUserId())
                .type(FolderType.PHRASE)
                .imageFile(file)
                .build();

        //when
        Folder folder = folderService.createFolder(folderDTO);

        //then
        Assertions.assertThat(folder.getFolderId()).isNotNull();
        Assertions.assertThat(folder.getFolderName()).isEqualTo("파일A");
        Assertions.assertThat(folder.getUser().getUserId()).isEqualTo(user.getUserId());
        Assertions.assertThat(folder.getType()).isEqualTo(FolderType.PHRASE);
        Assertions.assertThat(folder.getFolderPath()).isNotBlank();
        Assertions.assertThat(folder.getStoreFilename().substring(folder.getStoreFilename().lastIndexOf("_")+1))
                .isEqualTo(file.getOriginalFilename());
    }

    @Test
    void 폴더생성_이미지x() throws Exception{
        //given
        User user = createUser();

        FolderDTO folderDTO = FolderDTO.builder()
                .folderName("파일A")
                .userId(user.getUserId())
                .type(FolderType.PHRASE)
                .build();

        //when
        Folder folder = folderService.createFolder(folderDTO);

        //then
        Assertions.assertThat(folder.getFolderId()).isNotNull();
        Assertions.assertThat(folder.getFolderName()).isEqualTo("파일A");
        Assertions.assertThat(folder.getUser().getUserId()).isEqualTo(user.getUserId());
        Assertions.assertThat(folder.getType()).isEqualTo(FolderType.PHRASE);
        Assertions.assertThat(folder.getFolderPath()).isEqualTo("");
        Assertions.assertThat(folder.getStoreFilename()).isBlank();
    }



    @Test
    void 폴더_이름변경() throws Exception{
        //given
        User user = createUser();

        //폴더생성
        Folder folder = Folder.builder()
                .user(user)
                .folderName("파일A")
                .type(FolderType.PHRASE)
                .build();

        folderRepository.save(folder);


        //when
        folderService.modifyFolderName(folder.getFolderId(), "이름변경");

        //then
        Assertions.assertThat(folder.getFolderName()).isEqualTo("이름변경");
    }

    @Test
    void 빈폴더삭제() throws Exception{
        //given
        User user = createUser();

        Folder folder = Folder.builder()
                .user(user)
                .folderName("폴더A")
                .type(FolderType.PHRASE)
                .storeFilename("asdfsfss")
                .build();

        folderRepository.save(folder);

        //when
        folderService.removeFolder(folder.getFolderId());

        //then
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> folderRepository.findById(folder.getFolderId()).get());

        Assertions.assertThat(e.getMessage()).isEqualTo("No value present");
    }

    @Test
    void 글귀존재_폴더삭제() throws Exception{
        //given
        User user = createUser();

        Folder folder = Folder.builder()
                .user(user)
                .folderName("폴더A")
                .type(FolderType.PHRASE)
                .build();

        folderRepository.save(folder);

        Phrase phrase = Phrase.builder()
                .user(user)
                .folder(folder)
                .text("글귀입니다.")
                .build();
        phraseRepository.save(phrase);


        //when
        folderService.removeFolder(folder.getFolderId());

        //then
        //삭제된 폴더에 속한 글귀 레코드 검색
        NoSuchElementException e1 = assertThrows(NoSuchElementException.class,
                () -> phraseRepository.findById(phrase.getPhraseId()).get());

        //삭제된 폴더 검색
        NoSuchElementException e2 = assertThrows(NoSuchElementException.class,
                () -> folderRepository.findById(folder.getFolderId()).get());

        Assertions.assertThat(e1.getMessage()).isEqualTo("No value present");
        Assertions.assertThat(e2.getMessage()).isEqualTo("No value present");
    }

    @Test
    void 링크존재_폴더삭제() throws Exception{
        //given
        User user = createUser();

        Folder folder = Folder.builder()
                .user(user)
                .folderName("폴더A")
                .type(FolderType.LINK)
                .build();

        folderRepository.save(folder);

        Link link = Link.builder()
                .user(user)
                .folder(folder)
                .link("jamsil_team.com")
                .build();
        linkRepository.save(link);

        //when
        folderService.removeFolder(folder.getFolderId());

        //then
        //삭제된 폴더에 속한 링크 레코드 검색
        NoSuchElementException e1 = assertThrows(NoSuchElementException.class,
                () -> linkRepository.findById(link.getLinkId()).get());

        //삭제된 폴더 검색
        NoSuchElementException e2 = assertThrows(NoSuchElementException.class,
                () -> folderRepository.findById(folder.getFolderId()).get());

        Assertions.assertThat(e1.getMessage()).isEqualTo("No value present");
        Assertions.assertThat(e2.getMessage()).isEqualTo("No value present");
    }

    @Test
    void 폴더_리스트() throws Exception{
        //given
        User user = createUser();
        Folder folderA = Folder.builder()
                .folderName("폴더A")
                .user(user)
                .type(FolderType.PHRASE)
                .build();

        folderRepository.save(folderA); //userId: 형우, type: Phrase, parentFolder: null

        Folder folderB = Folder.builder()
                .folderName("폴더B")
                .user(user)
                .type(FolderType.PHRASE)
                .parentFolder(folderA)
                .build();

        folderRepository.save(folderB); //userId: 형우, type: Phrase, parentFolder: folderA

        Folder folderC = Folder.builder()
                .folderName("폴더C")
                .user(user)
                .type(FolderType.LINK)
                .build();

        folderRepository.save(folderC); //userId: 형우, type: Link, parentFolder: null

        //when 폴더A 1개만 출력 기대
        List<FolderResDTO> result = folderService.getListOfFolder("형우", FolderType.PHRASE, null);

        //then
        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(0).getFolderId()).isEqualTo(folderA.getFolderId());
        Assertions.assertThat(result.get(0).getFolderName()).isEqualTo(folderA.getFolderName());
        Assertions.assertThat(result.get(0).getUserId()).isEqualTo(folderA.getUser().getUserId());
        Assertions.assertThat(result.get(0).getParentFolderId()).isNull();
        Assertions.assertThat(result.get(0).getType()).isEqualTo(folderA.getType());
    }

    @Test
    void 글귀폴더_상세조회() throws Exception{
        //given
        User user = createUser();

        Folder folderA = Folder.builder()
                .folderName("폴더A")
                .user(user)
                .type(FolderType.PHRASE)
                .build();

        folderRepository.save(folderA); //userId: 형우, type: Phrase, parentFolder: null

        //폴더A 안에 글귀 2, 폴더 1 추가
        Phrase phraseA = Phrase.builder()
                .user(user)
                .folder(folderA)
                .text("폴더 상세조회 테스트 하기")
                .build();

        phraseRepository.save(phraseA); //userId: 형우,folder: folderA

        Phrase phraseB = Phrase.builder()
                .user(user)
                .folder(folderA)
                .text("폴더 상세조회 테스트 하기")
                .build();

        phraseRepository.save(phraseB); //userId: 형우,folder: folderA

        Folder folderB = Folder.builder()
                .folderName("폴더B")
                .user(user)
                .parentFolder(folderA)
                .type(FolderType.PHRASE)
                .build();

        folderRepository.save(folderB); //userId: 형우, type: Phrase, parentFolder: folderA

        //when
        //폴더 A 조회
        DetailFolderDTO detailFolderDTO =
                folderService.getFolder(user.getUserId(), folderA.getFolderId());

        //then
        List<PhraseDTO> phraseDTOList = detailFolderDTO.getPhraseDTOList();
        List<FolderResDTO> folderResDTOList = detailFolderDTO.getFolderResDTOList();
        //글귀 2, 폴더 1
        Assertions.assertThat(phraseDTOList.size()).isEqualTo(2);
        Assertions.assertThat(folderResDTOList.size()).isEqualTo(1);

    }

    @Test
    void 링크폴더_상세조회() throws Exception{
        //given
        User user = createUser();

        Folder folderA = Folder.builder()
                .folderName("폴더A")
                .user(user)
                .type(FolderType.LINK)
                .build();

        folderRepository.save(folderA); //userId: 형우, type: link, parentFolder: null

        //폴더A 안에 글귀x, 폴더 1만 추가

        Folder folderB = Folder.builder()
                .folderName("폴더B")
                .user(user)
                .parentFolder(folderA)
                .type(FolderType.LINK)
                .build();

        folderRepository.save(folderB); //userId: 형우, type: link, parentFolder: folderA

        //when
        //폴더 A 조회
        DetailFolderDTO detailFolderDTO =
                folderService.getFolder(user.getUserId(), folderA.getFolderId());
        
        //then
        List<LinkDTO> linkDTOList = detailFolderDTO.getLinkDTOList();
        List<FolderResDTO> folderResDTOList = detailFolderDTO.getFolderResDTOList();
        //링크 0, 폴더 1
        Assertions.assertThat(linkDTOList.size()).isEqualTo(0);
        Assertions.assertThat(folderResDTOList.size()).isEqualTo(1);

    }

    private User createUser() {

        User user = User.builder()
                .userId("형우")
                .password("1111")
                .phone("010-0000-0000")
                .build();

        userRepository.save(user);

        return user;
    }


}