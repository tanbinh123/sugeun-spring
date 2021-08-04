package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.folder.FolderRepository;
import com.jamsil_team.sugeun.domain.link.Link;
import com.jamsil_team.sugeun.domain.link.LinkRepository;
import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.domain.user.UserRepository;
import com.jamsil_team.sugeun.dto.link.LinkDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class LinkServiceImplTest {

    @Autowired LinkService linkService;
    @Autowired UserRepository userRepository;
    @Autowired FolderRepository folderRepository;
    @Autowired LinkRepository linkRepository;

    @Test
    void 링크생성() throws Exception{
        //given
        User user = createUser();

        LinkDTO linkDTO = LinkDTO.builder()
                .userId(user.getUserId())
                .link("jamsil_team.com")
                .build();

        //when
        Link link = linkService.createLink(linkDTO);

        //then
        Assertions.assertThat(link.getLinkId()).isNotNull();
        Assertions.assertThat(link.getLink()).isEqualTo(linkDTO.getLink());
        Assertions.assertThat(link.getBookmark()).isFalse();
        Assertions.assertThat(link.getFolder()).isNull();
        Assertions.assertThat(link.getUser().getUserId()).isEqualTo(user.getUserId());
    }

    @Test
    void 링크_북마크등록() throws Exception{
        //given
        User user = createUser();

        Link link = Link.builder()
                .user(user)
                .link("jamsil_team.com")
                .build();

        linkRepository.save(link);

        //when
        linkService.modifyBookmark(link.getLinkId());

        //then
        Assertions.assertThat(link.getBookmark()).isTrue();
    }

    @Test
    void 링크_북마크취소() throws Exception{
        //given
        User user = createUser();

        Link link = Link.builder()
                .user(user)
                .link("jamsil_team.com")
                .bookmark(true)
                .build();

        linkRepository.save(link);

        //when
        linkService.modifyBookmark(link.getLinkId());

        //then
        Assertions.assertThat(link.getBookmark()).isFalse();
    }

    @Test
    void 링크삭제() throws Exception{
        //given
        User user = createUser();

        Link link = Link.builder()
                .user(user)
                .link("jamsil_team.com")
                .build();

        linkRepository.save(link);

        //when
        linkService.removeLink(link.getLinkId());

        //then
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> (linkRepository.findById(link.getLinkId())).get());

        Assertions.assertThat(e.getMessage()).isEqualTo("No value present");
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