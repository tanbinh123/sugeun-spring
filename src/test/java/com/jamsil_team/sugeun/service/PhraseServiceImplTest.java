package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.domain.folder.FolderRepository;
import com.jamsil_team.sugeun.domain.folder.FolderType;
import com.jamsil_team.sugeun.domain.phrase.Phrase;
import com.jamsil_team.sugeun.domain.phrase.PhraseRepository;
import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.domain.user.UserRepository;
import com.jamsil_team.sugeun.dto.phrase.PhraseDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class PhraseServiceImplTest {

    @Autowired PhraseService phraseService;
    @Autowired UserRepository userRepository;
    @Autowired FolderRepository folderRepository;
    @Autowired PhraseRepository phraseRepository;

    @Test
    void 글귀생성() throws Exception{
        //given
        Folder folder = createFolder();
        User user = folder.getUser();

        PhraseDTO phraseDTO = PhraseDTO.builder()
                .userId(user.getUserId())
                .folderId(folder.getFolderId())
                .text("나의 첫 글귀")
                .build();

        //when
        Phrase phrase = phraseService.createPhrase(phraseDTO);

        //then
        Assertions.assertThat(phrase.getPhraseId()).isNotNull();
        Assertions.assertThat(phrase.getText()).isEqualTo(phraseDTO.getText());
        Assertions.assertThat(phrase.getFolder().getFolderId()).isEqualTo(folder.getFolderId());
        Assertions.assertThat(phrase.getTextDate()).isEqualTo(LocalDate.now());
        Assertions.assertThat(phrase.getBookmark()).isFalse();
    }

    @Test
    void 글귀_TEXT변경() throws Exception{
        //given
        Folder folder = createFolder();
        User user = folder.getUser();

        Phrase phrase = Phrase.builder()
                .user(user)
                .folder(folder)
                .text("나의 첫 글귀")
                .textDate(LocalDate.of(2021,07,16))
                .build();
        phraseRepository.save(phrase);

        //when
        phraseService.ModifyPhraseText(phrase.getPhraseId(), "글귀변경");

        //then
        Assertions.assertThat(phrase.getText()).isEqualTo("글귀변경");
        Assertions.assertThat(phrase.getTextDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void 북마크_등록() throws Exception{
        //given
        Folder folder = createFolder();
        User user = folder.getUser();

        Phrase phrase = Phrase.builder()
                .user(user)
                .folder(folder)
                .build();
        phraseRepository.save(phrase);

        //when
        phraseService.ModifyBookmark(phrase.getPhraseId());

        //then
        Assertions.assertThat(phrase.getBookmark()).isTrue();

    }

    @Test
    void 북마크_취소() throws Exception{
        //given
        Folder folder = createFolder();
        User user = folder.getUser();

        Phrase phrase = Phrase.builder()
                .user(user)
                .folder(folder)
                .bookmark(true)
                .build();
        phraseRepository.save(phrase);

        //when
        phraseService.ModifyBookmark(phrase.getPhraseId());

        //then
        Assertions.assertThat(phrase.getBookmark()).isFalse();
    }

    @Test
    void 글귀삭제() throws Exception{
        //given
        Folder folder = createFolder();
        User user = folder.getUser();

        Phrase phrase = Phrase.builder()
                .user(user)
                .folder(folder)
                .text("나의 첫 글귀")
                .build();
        phraseRepository.save(phrase);

        //when
        phraseService.removePhrase(phrase.getPhraseId());

        //then
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> (phraseRepository.findById(phrase.getPhraseId())).get());

        Assertions.assertThat(e.getMessage()).isEqualTo("No value present");
    }


    private Folder createFolder() {
        User user = User.builder()
                .nickname("형우B")
                .password("1111")
                .phone("010-0000-0000")
                .build();

        userRepository.save(user);

        Folder folder = Folder.builder().
                folderName("파일A")
                .user(user)
                .type(FolderType.PHRASE)
                .build();

        folderRepository.save(folder);

        return folder;
    }
}