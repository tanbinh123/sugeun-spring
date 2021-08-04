package com.jamsil_team.sugeun.domain.folder;

import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.domain.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class FolderRepositoryTest {

    @Autowired FolderRepository folderRepository;
    @Autowired UserRepository userRepository;

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

        //when
        List<Folder> result = folderRepository.getListFolder(user.getUserId(), FolderType.PHRASE, folderA.getFolderId());

        for(Folder folder : result){
            System.out.println(folder);
        }

        //then
        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(0).getFolderName()).isEqualTo(folderB.getFolderName());
        Assertions.assertThat(result.get(0).getUser()).isEqualTo(folderB.getUser());
        Assertions.assertThat(result.get(0).getParentFolder()).isEqualTo(folderA);
    }

    private User createUser() {

        User user = User.builder()
                .nickname("형우")
                .password("1111")
                .phone("010-0000-0000")
                .build();

        userRepository.save(user);

        return user;
    }


}