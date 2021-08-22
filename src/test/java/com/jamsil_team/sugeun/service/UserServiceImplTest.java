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
import com.jamsil_team.sugeun.dto.user.BookmarkDTO;
import com.jamsil_team.sugeun.dto.user.UserResDTO;
import com.jamsil_team.sugeun.dto.user.UserSignupDTO;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class UserServiceImplTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired FolderRepository folderRepository;
    @Autowired PhraseRepository phraseRepository;
    @Autowired LinkRepository linkRepository;

    @Test
    void 중복확인() throws Exception{
        //given
        UserSignupDTO signUpDTOUser = createSignUpDTO();

        User user = signUpDTOUser.toEntity();
        userRepository.save(user);

        //when
        Boolean result = userService.isDuplicateNickname("형우2");

        //then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void 회원가입() throws Exception{
        //given
        UserSignupDTO signUpDTOUser = createSignUpDTO();

        String rawPassword = signUpDTOUser.getPassword();
        //when
        User user = userService.join(signUpDTOUser);

        //then
        Assertions.assertThat(user.getUserId()).isNotNull();
        Assertions.assertThat(user.getNickname()).isEqualTo(signUpDTOUser.getNickname());

        boolean matches = passwordEncoder.matches(rawPassword, user.getPassword());
        Assertions.assertThat(matches).isTrue();

        Assertions.assertThat(user.getDeviceToken()).isNull();
    }

    @Test
    void 회원가입_실패() throws Exception{
        //given
        //기존 등록된 아이디
        UserSignupDTO userSignupDTO1 = createSignUpDTO();// loginId = 형우
        User user1 = userSignupDTO1.toEntity();
        userRepository.save(user1);

        //회원가입
        UserSignupDTO userSignupDTO2 = createSignUpDTO();// loginId = 형우

        //when
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> userService.join(userSignupDTO2));

        //then
        Assertions.assertThat(e.getMessage()).isEqualTo("이미 등록된 ID 입니다.");
    }


    @Test
    void 기존비밀번호_검증_성공() throws Exception{
        //given
        User user = User.builder()
                .nickname("형우")
                .password(passwordEncoder.encode("1111"))
                .phone("010-0000-0000")
                .deviceToken("adsf1r@Afdfas")
                .build();

        userRepository.save(user);

        //when
        Boolean result = userService.verifyPassword(user.getUserId(), "1111");

        //then
        Assertions.assertThat(result).isTrue();
    }




    @Test
    void 기존비밀번호_검증_실패() throws Exception{
        //given
        User user = User.builder()
                .nickname("형우")
                .password(passwordEncoder.encode("1111"))
                .phone("010-0000-0000")
                .deviceToken("adsf1r@Afdfas")
                .build();

        userRepository.save(user);

        //when
        Boolean result = userService.verifyPassword(user.getUserId(), "2222");

        //then
        Assertions.assertThat(result).isFalse();

    }


    @Test
    void 프로필사진_업데이트_기존x() throws Exception{
        //given
        User user = User.builder()
                .nickname("형우")
                .password(passwordEncoder.encode("1111"))
                .phone("010-0000-0000")
                .deviceToken("adsf1r@Afdfas")
                .build();

        userRepository.save(user);

        MockMultipartFile file = new MockMultipartFile("file", "filename-1.jpeg", "image/jpeg", "some-image".getBytes());

        //when
        userService.modifyUserImg(user.getUserId(), file);

        //then
        User savedUser = userRepository.findById(user.getUserId()).get();
        String storeFilename = savedUser.getStoreFilename();

        Assertions.assertThat(savedUser.getFolderPath()).isNotBlank();
        Assertions.assertThat(storeFilename.substring(storeFilename.lastIndexOf("_")+1)).isEqualTo(file.getOriginalFilename());

    }

    @Test
    void 프로필사진_업데이트_기존o() throws Exception{
        //given
        User user = User.builder()
                .nickname("형우")
                .password(passwordEncoder.encode("1111"))
                .phone("010-0000-0000")
                .deviceToken("adsf1r@Afdfas")
                .build();

        userRepository.save(user);

        MockMultipartFile file1 = new MockMultipartFile("file", "filename-1.jpeg", "image/jpeg", "some-image".getBytes());
        //savedUserB의 folderPath 와 비교
        User savedUserA = userRepository.findById(user.getUserId()).get();


        //프로필 사진 저장
        userService.modifyUserImg(user.getUserId(), file1);

        MockMultipartFile file2 = new MockMultipartFile("file", "filename-2.jpeg", "image/jpeg", "some-image".getBytes());

        //when
        userService.modifyUserImg(user.getUserId(), file2);

        //then
        User savedUserB = userRepository.findById(user.getUserId()).get();
        String storeFilename = savedUserB.getStoreFilename();

        Assertions.assertThat(savedUserB.getFolderPath()).isNotNull();
        Assertions.assertThat(savedUserB.getFolderPath()).isNotBlank();
        Assertions.assertThat(savedUserB.getFolderPath()).isEqualTo(savedUserA.getFolderPath());
        Assertions.assertThat(storeFilename.substring(storeFilename.lastIndexOf("_")+1)).isEqualTo(file2.getOriginalFilename());
    }

    @Test
    void 프로필사진_업데이트_기존o_변경값NULL() throws Exception{
        //given
        User user = User.builder()
                .nickname("형우")
                .password(passwordEncoder.encode("1111"))
                .phone("010-0000-0000")
                .deviceToken("adsf1r@Afdfas")
                .build();

        userRepository.save(user);

        MockMultipartFile file = new MockMultipartFile("file", "filename-1.jpeg", "image/jpeg", "some-image".getBytes());

        //when
        userService.modifyUserImg(user.getUserId(), null);

        //then
        User savedUser = userRepository.findById(user.getUserId()).get();

        Assertions.assertThat(savedUser.getFolderPath()).isBlank();
        Assertions.assertThat(savedUser.getStoreFilename()).isBlank();
    }

    @Test
    void 프로필사진_업데이트_이미지파일x() throws Exception{
        //given
        User user = User.builder()
                .nickname("형우")
                .password(passwordEncoder.encode("1111"))
                .phone("010-0000-0000")
                .deviceToken("adsf1r@Afdfas")
                .build();

        userRepository.save(user);

        MockMultipartFile file = new MockMultipartFile("file", "filename-1.jpeg", "json/jpeg", "some-image".getBytes());

        //when
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> userService.modifyUserImg(user.getUserId(), file));

        //then
        Assertions.assertThat(e.getMessage()).isEqualTo("이미지 파일이 아닙니다.");
    }


    @Test
    void 아이디_변경() throws Exception{
        //given
        User user = User.builder()
                .nickname("형우")
                .password(passwordEncoder.encode("1111"))
                .phone("010-0000-0000")
                .deviceToken("adsf1r@Afdfas")
                .build();

        userRepository.save(user);

        //when
        userService.modifyUserId(user.getUserId(), "수근수근");

        //then
        Assertions.assertThat(userRepository.findByNickname("수근수근")).isNotNull();

        User savedUser = userRepository.findById(user.getUserId()).get();
        Assertions.assertThat(savedUser.getNickname()).isEqualTo("수근수근");

        boolean matches = passwordEncoder.matches("1111", savedUser.getPassword());
        Assertions.assertThat(matches).isTrue();

        Assertions.assertThat(savedUser.getPhone()).isEqualTo("010-0000-0000");
        Assertions.assertThat(savedUser.getDeviceToken()).isEqualTo("adsf1r@Afdfas");
    }



    @Test
    void 비밀번호_변경() throws Exception{
        //given
        User user = User.builder()
                .nickname("형우")
                .password(passwordEncoder.encode("1111"))
                .phone("010-0000-0000")
                .deviceToken("adsf1r@Afdfas")
                .build();

        userRepository.save(user);

        //when
        userService.modifyPassword(user.getUserId(), "2222");

        //then
        Optional<User> result = userRepository.findById(user.getUserId());
        User savedUser = result.get();

        boolean matches = passwordEncoder.matches("2222", savedUser.getPassword());

        Assertions.assertThat(matches).isTrue();
    }

    @Test
    void 프로필_조회() throws Exception{
        //given
        User user = User.builder()
                .nickname("형우")
                .password(passwordEncoder.encode("1111"))
                .phone("010-0000-0000")
                .deviceToken("adsf1r@Afdfas")
                .build();

        userRepository.save(user);

        //when
        UserResDTO userResDTO = userService.getUser(user.getUserId());

        //then
        Assertions.assertThat(userResDTO.getUserId()).isEqualTo(user.getUserId());
        Assertions.assertThat(userResDTO.getAlarm()).isEqualTo(user.getAlarm());
        Assertions.assertThat(userResDTO.getPhone()).isEqualTo(user.getPhone());
//        Assertions.assertThat(userDTO.getFolderPath()).isEqualTo(user.getFolderPath());
//        Assertions.assertThat(userDTO.getStoreFilename()).isEqualTo(user.getStoreFilename());
    }

    @Test
    void 알람허용_변경_기존_true() throws Exception{
        //given
        User user = User.builder()
                .nickname("형우")
                .password(passwordEncoder.encode("1111"))
                .phone("010-0000-0000")
                .alarm(true) //알람 허용o
                .deviceToken("adsf1r@Afdfas")
                .build();

        userRepository.save(user);

        //when
        userService.modifyAlarm(user.getUserId());

        //then
        Optional<User> result = userRepository.findById(user.getUserId());
        User savedUser = result.get();

        Assertions.assertThat(savedUser.getAlarm()).isFalse();
    }
    
    @Test
    void 알람허용_변경_기존_false() throws Exception{
        //given
        User user = User.builder()
                .nickname("형우")
                .password(passwordEncoder.encode("1111"))
                .phone("010-0000-0000")
                .alarm(false) //알람 허용x
                .deviceToken("adsf1r@Afdfas")
                .build();

        userRepository.save(user);

        //when
        userService.modifyAlarm(user.getUserId());

        //then
        Optional<User> result = userRepository.findById(user.getUserId());
        User savedUser = result.get();

        Assertions.assertThat(savedUser.getAlarm()).isTrue();
    }

    @Test
    void 북마크_리스트() throws Exception{
        //given
        User user = User.builder()
                .nickname("형우")
                .password(passwordEncoder.encode("1111"))
                .phone("010-0000-0000")
                .deviceToken("adsf1r@Afdfas")
                .build();

        userRepository.save(user);
        //폴더 생성
        Folder folderA = createFolder(user, FolderType.PHRASE); //글귀 폴더
        Folder folderB = createFolder(user, FolderType.LINK); //링크 폴더

        //글귀 true : 1, false: 1 , 링크 true : 0, false : 1
        Phrase phraseTrue = Phrase.builder()
                .user(user)
                .folder(folderA)
                .text("북마크 테스트")
                .bookmark(true) //true
                .build();

        phraseRepository.save(phraseTrue);

        Phrase phraseFalse = Phrase.builder()
                .user(user)
                .folder(folderA)
                .text("북마크 테스트")
                .bookmark(false) //true
                .build();

        phraseRepository.save(phraseFalse);

        Link linkFalse = Link.builder()
                .user(user)
                .folder(folderB)
                .title("링크제목 test")
                .link("북마크 테스트")
                .bookmark(false) //false
                .build();

        linkRepository.save(linkFalse);

        //when
        BookmarkDTO bookmarkDTO = userService.getListOfBookmark(user.getUserId());

        //then phrase.bookmark = true 인 글귀 1개만 출력, link 빈 리스트 출력
        Assertions.assertThat(bookmarkDTO.getPhraseResDTOList().size()).isEqualTo(1);
        Assertions.assertThat(bookmarkDTO.getPhraseResDTOList().get(0).getBookmark()).isTrue();
        Assertions.assertThat(bookmarkDTO.getLinkResDTOList()).isEmpty();
    }

    @Test
    void 아이디찾기_회원_존재o() throws Exception{
        //given
        User user = User.builder()
                .nickname("형우")
                .password(passwordEncoder.encode("1111"))
                .phone("010-0000-0000")
                .deviceToken("adsf1r@Afdfas")
                .build();

        userRepository.save(user);

        //when
        String nickname = userService.findNickname(user.getPhone());

        //then
        Assertions.assertThat(nickname).isEqualTo(user.getNickname());
    }

    @Test
    void 아이디찾기_회원_존재x() throws Exception{
        //given
        //폰 넘버: 010-0000-0000 인 회원은 존재하지 않는다.
        //when
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> userService.findNickname("010-0000-0000"));

        //then
        Assertions.assertThat(e.getMessage()).isEqualTo("존재하지 않은 회원입니다.");
    }

    @Test
    void 비밀번호찾기_아이디체크_존재O(){

        //given
        User user = User.builder()
                .nickname("형우")
                .password(passwordEncoder.encode("1111"))
                .phone("010-0000-0000")
                .deviceToken("adsf1r@Afdfas")
                .build();

        userRepository.save(user);

        //when
        Long userId = userService.checkNickname("형우");

        //then
        Assertions.assertThat(userId).isEqualTo(user.getUserId());

    }

    @Test
    void 비밀번호찾기_아이디체크_존재x() throws Exception{
        //given
        //nickname: 형우 인 회원은 존재하지 않는다.

        //when
        Long userId = userService.checkNickname("형우");

        //then
        Assertions.assertThat(userId).isEqualTo(-1);
    }
    
    @Test
    void 비밀번호찾기_핸드폰번호검증_동일o() throws Exception{
        //given
        User user = User.builder()
                .nickname("형우")
                .password(passwordEncoder.encode("1111"))
                .phone("010-0000-0000")
                .deviceToken("adsf1r@Afdfas")
                .build();

        userRepository.save(user);

        //when
        Boolean result = userService.verifyPhone(user.getUserId(), "010-0000-0000");

        //then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void 비밀번호찾기_핸드폰번호검증_동일x() throws Exception{
        //given
        User user = User.builder()
                .nickname("형우")
                .password(passwordEncoder.encode("1111"))
                .phone("010-0000-0000")
                .deviceToken("adsf1r@Afdfas")
                .build();

        userRepository.save(user);

        //when
        Boolean result = userService.verifyPhone(user.getUserId(), "010-1111-1111");

        //then
        Assertions.assertThat(result).isFalse();
    }

    private Folder createFolder(User user, FolderType type) {
        Folder folder = Folder.builder()
                .user(user)
                .type(type)
                .folderName("폴더")
                .build();

        folderRepository.save(folder);

        return folder;
    }


    private UserSignupDTO createSignUpDTO() {
        UserSignupDTO signUpDTOUser = UserSignupDTO.builder()
                .nickname("형우")
                .password("1111")
                .phone("010-0000-0000")
                .build();

        return signUpDTOUser;
    }
}