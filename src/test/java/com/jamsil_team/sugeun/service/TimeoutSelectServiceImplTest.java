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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class TimeoutSelectServiceImplTest {

    @Autowired TimeoutSelectService timeoutSelectService;
    @Autowired TimeoutRepository timeoutRepository;
    @Autowired UserRepository userRepository;
    @Autowired TimeoutService timeoutService;
    @Autowired TimeoutSelectRepository timeoutSelectRepository;

    @Test
    void 타임아웃_알람변경() throws Exception{
        //given
        //타임아웃, 알람 생성
        User user = createUser();
        List<Integer> selected = List.of(1, 3); //유효기간 1,3일 전

        TimeoutDTO timeoutDTO = TimeoutDTO.builder()
                .userId(user.getUserId())
                .title("스타벅스 아메리카노")
                .deadline(LocalDateTime.of(2021, 8, 11, 23, 59))
                .selected(selected) //타임아웃 1,3일 전 생성
                .fileName("timeoutImg")
                .filePath("/hyeongwoo")
                .uuid(UUID.randomUUID().toString())
                .build();

        Timeout timeout = timeoutService.createTimeout(timeoutDTO);

        //when
        timeoutSelectService.modifyAlarm(timeout.getTimeoutId(), List.of(2)); //1,3 -> 2일 전으로 변경

        //then
        //알람 갯수 1개
        Assertions.assertThat(timeoutSelectRepository.alarmCountByTimeoutId(timeout.getTimeoutId())).isEqualTo(1);

        //알람일시 2021.08.09 12:00
        List<TimeoutSelect> result = timeoutSelectRepository.findByTimeoutId(timeout.getTimeoutId());
        Assertions.assertThat(result.get(0).getAlarmDateTime())
                .isEqualTo(LocalDateTime.of(2021,8,9,12,00));

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