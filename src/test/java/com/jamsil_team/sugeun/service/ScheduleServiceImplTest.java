package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.schedule.Schedule;
import com.jamsil_team.sugeun.domain.scheduleSelect.ScheduleSelect;
import com.jamsil_team.sugeun.domain.scheduleSelect.ScheduleSelectRepository;
import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.domain.user.UserRepository;
import com.jamsil_team.sugeun.dto.ScheduleDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
@SpringBootTest
class ScheduleServiceImplTest {

    @Autowired ScheduleService scheduleService;
    @Autowired UserRepository userRepository;
    @Autowired ScheduleSelectRepository scheduleSelectRepository;

    @Test
    void 스케줄생성() throws Exception{
        //given
        User user = createUser();
        List<Integer> selected = List.of(1, 3);

        ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                .userId(user.getUserId())
                .title("2시 잠실역")
                .selected(selected) //1,3일 전 알림
                .scheduleDate(LocalDateTime.of(2021, 7, 21, 14, 00))
                .build();

        //when
        Schedule schedule = scheduleService.createSchedule(scheduleDTO);

        //then
        //schedule 검증
        Assertions.assertThat(schedule.getScheduleId()).isNotNull();
        Assertions.assertThat(schedule.getUser().getUserId()).isEqualTo(user.getUserId());
        Assertions.assertThat(schedule.getTitle()).isEqualTo("2시 잠실역");
        Assertions.assertThat(schedule.getScheduleDate())
                .isEqualTo(LocalDateTime.of(2021, 7, 21, 14, 00));

        //scheduleSelect 알람 검증
        List<ScheduleSelect> result = scheduleSelectRepository.findByUserId(user.getUserId());

        //selected.size() == 2 (1,3일 전)
        Assertions.assertThat(result.size()).isEqualTo(2);

        //일정일: 2021.07.21 14:00 -> selected(0) == 1일 전 -> 알람일: 2021.07.20 12:00
        Assertions.assertThat(result.get(0).getAlarmDateTime())
                .isEqualTo(LocalDateTime.of(2021,7,20,12,00));

        //일정일: 2021.07.21 14:00 -> selected(1) == 3일 전 -> 알람일: 2021.07.18 12:00
        Assertions.assertThat(result.get(1).getAlarmDateTime())
                .isEqualTo(LocalDateTime.of(2021,7,18,12,00));
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