package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.domain.folder.FolderRepository;
import com.jamsil_team.sugeun.domain.folder.FolderType;
import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.domain.user.UserRepository;
import com.jamsil_team.sugeun.dto.FolderDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class FolderServiceImplTest {

    @Autowired FolderService folderService;
    @Autowired UserRepository userRepository;
    @Autowired FolderRepository folderRepository;

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
        folderService.folderNameModify(folder.getFolderId(), "이름변경");

        //then
        Assertions.assertThat(folder.getFolderName()).isEqualTo("이름변경");
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