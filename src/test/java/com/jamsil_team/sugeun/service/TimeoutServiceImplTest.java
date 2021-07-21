package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.timeout.Timeout;
import com.jamsil_team.sugeun.domain.timeout.TimeoutRepository;
import com.jamsil_team.sugeun.domain.timeoutSelect.TimeoutSelect;
import com.jamsil_team.sugeun.domain.timeoutSelect.TimeoutSelectRepository;
import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.domain.user.UserRepository;
import com.jamsil_team.sugeun.dto.TimeoutDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.TimeoutDeferredResultProcessingInterceptor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class TimeoutServiceImplTest {

    @Autowired TimeoutService timeoutService;
    @Autowired UserRepository userRepository;
    @Autowired TimeoutRepository timeoutRepository;
    @Autowired TimeoutSelectRepository timeoutSelectRepository;


    @Test
    void 타임아웃생성() throws Exception{
        //given
        User user = createUser();
        List<Integer> selected = List.of(1, 3); //유효기간 1,3일 전

        TimeoutDTO timeoutDTO = TimeoutDTO.builder()
                .userId(user.getUserId())
                .title("스타벅스 아메리카노")
                .deadline(LocalDateTime.of(2021, 8, 11, 23, 59))
                .selected(selected)
                .fileName("timeoutImg")
                .filePath("/hyeongwoo")
                .uuid(UUID.randomUUID().toString())
                .build();


        //when
        Timeout timeout = timeoutService.createTimeout(timeoutDTO);

        //then
        //timeout 검증
        Assertions.assertThat(timeout.getTimeoutId()).isNotNull();
        Assertions.assertThat(timeout.getDeadline()).isEqualTo(timeoutDTO.getDeadline());
        Assertions.assertThat(timeout.getIsValid()).isTrue();

        //timeoutSelect 검증
        List<TimeoutSelect> result = timeoutSelectRepository.findByUserId(timeout.getUser().getUserId());

        //selected.size() == 2
        Assertions.assertThat(result.size()).isEqualTo(2);

        //마감일: 21.08.11 23:59 -> selected.get(0) == 1 -> 알람일: 21.08.10 12:00
        Assertions.assertThat(result.get(0).getAlarmDateTime())
                .isEqualTo(timeoutDTO.getDeadline()
                        .minusDays(selected.get(0)).minusHours(11).minusMinutes(59))
                .isEqualTo(LocalDateTime.of(2021,8,10,12,00));

        //마감일: 21.08.11 23:59 -> selected.get(1) == 3 -> 알람일: 21.08.08 12:00
        Assertions.assertThat(result.get(1).getAlarmDateTime())
                .isEqualTo(timeoutDTO.getDeadline()
                        .minusDays(selected.get(1)).minusHours(11).minusMinutes(59))
                .isEqualTo(LocalDateTime.of(2021,8,8,12,00));

    }

    @Test
    void 타임아웃생성_알람선택x() throws Exception{
        //given
        User user = createUser();

        TimeoutDTO timeoutDTO = TimeoutDTO.builder()
                .userId(user.getUserId())
                .title("스타벅스 아메리카노")
                .deadline(LocalDateTime.of(2021, 8, 11, 23, 59))
                .fileName("timeoutImg")
                .filePath("/hyeongwoo")
                .uuid(UUID.randomUUID().toString())
                .build();


        //when
        Timeout timeout = timeoutService.createTimeout(timeoutDTO);

        //then
        //timeout 검증
        Assertions.assertThat(timeout.getTimeoutId()).isNotNull();
        Assertions.assertThat(timeout.getDeadline()).isEqualTo(timeoutDTO.getDeadline());
        Assertions.assertThat(timeout.getIsValid()).isTrue();

        //timeoutSelect 검증
        List<TimeoutSelect> result = timeoutSelectRepository.findByUserId(timeout.getUser().getUserId());

        Assertions.assertThat(result).isEmpty();

    }

    @Test
    void 사용완료기능() throws Exception{
        //given
        User user = createUser();

        //타임아웃 생성
        Timeout timeout = Timeout.builder()
                .user(user)
                .title("스타벅스 아메리카노")
                .deadline(LocalDateTime.of(2021, 8, 11, 23, 59))
                .fileName("timeoutImg")
                .filePath("/hyeongwoo")
                .uuid(UUID.randomUUID().toString())
                .build();

        timeoutRepository.save(timeout);

        //when
        timeoutService.finishUse(timeout.getTimeoutId());

        //then
        //유효필드 false 확인
        Assertions.assertThat(timeout.getIsValid()).isFalse();

        //삭제된 타임아웃 알람 삭제 확인
        System.out.println(timeoutSelectRepository.findByTimeoutId(timeout.getTimeoutId()));

        Assertions.assertThat(timeoutSelectRepository.findByTimeoutId(timeout.getTimeoutId()).size()).isEqualTo(0);
        Assertions.assertThat(timeoutSelectRepository.findByTimeoutId(timeout.getTimeoutId())).isEmpty();

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