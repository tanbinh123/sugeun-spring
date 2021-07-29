package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.domain.user.UserRepository;
import com.jamsil_team.sugeun.dto.SignupDTO;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Test
    void 중복확인() throws Exception{
        //given
        SignupDTO signUpDTO = createSignUpDTO();
        User user = signUpDTO.toEntity();
        userRepository.save(user);

        //when
        Boolean result = userService.isDuplicateNickname("형우2");

        //then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void 회원가입() throws Exception{
        //given
        SignupDTO signUpDTO = createSignUpDTO();

        //when
        User user = userService.join(signUpDTO);

        //then
        Assertions.assertThat(user.getUserId()).isEqualTo("형우");
        Assertions.assertThat(user.getDeviceToken()).isNull();
    }

    @Test
    void 회원가입_실패() throws Exception{
        //given
        //기존 등록된 아이디
        SignupDTO signupDTO1 = createSignUpDTO();// loginId = 형우
        User user1 = signupDTO1.toEntity();
        userRepository.save(user1);

        //회원가입
        SignupDTO signupDTO2 = createSignUpDTO();// loginId = 형우

        //when
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> userService.join(signupDTO2));

        //then
        Assertions.assertThat(e.getMessage()).isEqualTo("이미 등록된 ID 입니다.");
    }

    /*
    @Test
    void 기존비밀번호_검증_성공() throws Exception{
        //given
        User user = User.builder()
                .userId("형우")
                .password(passwordEncoder.encode("1111"))
                .phone("010-0000-0000")
                .deviceToken("adsf1r@Afdfas")
                .build();

        userRepository.save(user);

        //when
        Boolean result = userService.verifyPassword("형우2", "1111");

        //then
        Assertions.assertThat(result).isTrue();
    }
    */


    /*
    @Test
    void 기존비밀번호_검증_실패() throws Exception{
        //given
        User user = User.builder()
                .userId("형우")
                .password(passwordEncoder.encode("1111"))
                .phone("010-0000-0000")
                .deviceToken("adsf1r@Afdfas")
                .build();

        userRepository.save(user);

        //when
        Boolean result = userService.verifyPassword("형우", "2222");

        //then
        Assertions.assertThat(result).isFalse();

    }

     */

    @Test
    void 비밀번호_변경() throws Exception{
        //given
        User user = User.builder()
                .userId("형우")
                .password(passwordEncoder.encode("1111"))
                .phone("010-0000-0000")
                .deviceToken("adsf1r@Afdfas")
                .build();

        userRepository.save(user);

        //when
        userService.modifyPassword("형우", "2222");

        //then
        Optional<User> result = userRepository.findByUserId("형우");
        User savedUser = result.get();

        boolean matches = passwordEncoder.matches("2222", savedUser.getPassword());

        Assertions.assertThat(matches).isTrue();
    }



    private SignupDTO createSignUpDTO() {
        SignupDTO signUpDTO = SignupDTO.builder()
                .userId("형우")
                .password("1111")
                .phone("010-0000-0000")
                .build();

        return signUpDTO;
    }
}