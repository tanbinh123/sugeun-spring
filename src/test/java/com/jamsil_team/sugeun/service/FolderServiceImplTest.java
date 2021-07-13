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
import com.jamsil_team.sugeun.dto.FolderDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

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
    void 폴더생성() throws Exception{
        //given
        User user = createUser();

        FolderDTO folderDTO = FolderDTO.builder()
                .folderName("파일A")
                .userId(user.getUserId())
                .type(FolderType.PHRASE)
                .fileName("fileImg")
                .filePath("/hyeongwoo")
                .uuid(UUID.randomUUID().toString())
                .build();

        //when
        Folder folder = folderService.createFolder(folderDTO);

        //then
        Assertions.assertThat(folder.getFolderId()).isNotNull();
        Assertions.assertThat(folder.getFolderName()).isEqualTo("파일A");
        Assertions.assertThat(folder.getUser().getUserId()).isEqualTo(user.getUserId());
        Assertions.assertThat(folder.getType()).isEqualTo(FolderType.PHRASE);
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
                .fileName("fileImg")
                .filePath("/hyeongwoo")
                .uuid(UUID.randomUUID().toString())
                .build();

        folderRepository.save(folder);


        //when
        folderService.ModifyFolderName(folder.getFolderId(), "이름변경");

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
                .fileName("folderImg")
                .filePath("/hyeongwoo")
                .uuid(UUID.randomUUID().toString())
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
                .fileName("folderImg")
                .filePath("/hyeongwoo")
                .uuid(UUID.randomUUID().toString())
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
                .fileName("folderImg")
                .filePath("/hyeongwoo")
                .uuid(UUID.randomUUID().toString())
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