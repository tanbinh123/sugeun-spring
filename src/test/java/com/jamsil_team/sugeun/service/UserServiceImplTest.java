package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.domain.user.UserRepository;
import com.jamsil_team.sugeun.dto.SignUpDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class UserServiceImplTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @Test
    void 중복확인() throws Exception{
        //given
        SignUpDTO signUpDTO = createSignUpDTO();
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
        SignUpDTO signUpDTO = createSignUpDTO();

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
        SignUpDTO signUpDTO1 = createSignUpDTO();// loginId = 형우
        User user1 = signUpDTO1.toEntity();
        userRepository.save(user1);

        //회원가입
        SignUpDTO signUpDTO2 = createSignUpDTO();// loginId = 형우

        //when
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> userService.join(signUpDTO2));

        //then
        Assertions.assertThat(e.getMessage()).isEqualTo("이미 등록된 ID 입니다.");
    }




    private SignUpDTO createSignUpDTO() {
        SignUpDTO signUpDTO = SignUpDTO.builder()
                .userId("형우")
                .password("1111")
                .phone("010-0000-0000")
                .build();

        return signUpDTO;
    }
}