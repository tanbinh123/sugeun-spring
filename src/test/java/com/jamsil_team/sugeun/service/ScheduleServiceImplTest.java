package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.schedule.Schedule;
import com.jamsil_team.sugeun.domain.schedule.ScheduleRepository;
import com.jamsil_team.sugeun.domain.scheduleSelect.ScheduleSelect;
import com.jamsil_team.sugeun.domain.scheduleSelect.ScheduleSelectRepository;
import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.domain.user.UserRepository;
import com.jamsil_team.sugeun.dto.schedule.ScheduleDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
@SpringBootTest
class ScheduleServiceImplTest {

    @Autowired ScheduleService scheduleService;
    @Autowired UserRepository userRepository;
    @Autowired ScheduleSelectRepository scheduleSelectRepository;
    @Autowired ScheduleRepository scheduleRepository;

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

    @Test
    void 스케줄_수정() throws Exception{
        //given
        User user = createUser();

        Schedule schedule = Schedule.builder()
                .user(user)
                .title("2시 30분 잠실역")
                .scheduleDate(LocalDateTime.of(2021, 7, 28, 14, 30))
                .build();

        scheduleRepository.save(schedule);

        //schedule 1, 3일전 알람 생성
        LocalDateTime before1 = schedule.getScheduleDate().minusDays(1).toLocalDate().atTime(12, 00);
        LocalDateTime before3 = schedule.getScheduleDate().minusDays(3).toLocalDate().atTime(12, 00);

        ScheduleSelect scheduleSelectA = ScheduleSelect.builder()
                .schedule(schedule) //schedule
                .alarmDateTime(before1) //1일전
                .selected(1)
                .build();

        scheduleSelectRepository.save(scheduleSelectA);

        ScheduleSelect scheduleSelectB = ScheduleSelect.builder()
                .schedule(schedule) //schedule
                .alarmDateTime(before3) //3일전
                .selected(3)
                .build();

        scheduleSelectRepository.save(scheduleSelectB);

        //제목 "2시 잠실역", 스케줄 날짜 "2021/07/28 14:00", 알람일 2일전
        ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                .scheduleId(schedule.getScheduleId())
                .userId(user.getUserId())
                .title("2시 잠실역")
                .scheduleDate(LocalDateTime.of(2021, 7, 28, 14, 00))
                .selected(List.of(2)) //2일전 선택
                .build();
        //when
        scheduleService.modifySchedule(scheduleDTO);

        //then
        //schedule 변경 검증
        Assertions.assertThat(schedule.getUser()).isEqualTo(user);
        Assertions.assertThat(schedule.getTitle()).isEqualTo(scheduleDTO.getTitle());
        Assertions.assertThat(schedule.getScheduleDate()).isEqualTo(scheduleDTO.getScheduleDate());

        //scheduleSelect 변경 검증
        List<ScheduleSelect> result = scheduleSelectRepository.findByScheduleId(schedule.getScheduleId());
        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(0).getSelected()).isEqualTo(2);

        //일정일: 2021.07.28 14:30 -> selected(0) == 2일 전 -> 알람일: 2021.07.26 12:00
        Assertions.assertThat(result.get(0).getAlarmDateTime())
                .isEqualTo(LocalDateTime.of(2021,7,26,12,00));
    }

    @Test
    void 스케줄_삭제() throws Exception{
        //given
        User user = createUser();

        Schedule schedule = Schedule.builder()
                .user(user)
                .title("2시 30분 잠실역")
                .scheduleDate(LocalDateTime.of(2021, 7, 28, 14, 30))
                .build();

        scheduleRepository.save(schedule);

        //schedule 1일전 알람 생성
        LocalDateTime before1 = schedule.getScheduleDate().minusDays(1).toLocalDate().atTime(12, 00);

        ScheduleSelect scheduleSelect = ScheduleSelect.builder()
                .schedule(schedule) //schedule
                .alarmDateTime(before1) //1일전
                .selected(1)
                .build();

        scheduleSelectRepository.save(scheduleSelect);

        //when
        scheduleService.removeSchedule(schedule.getScheduleId());

        //then
        //삭제된 스케줄의 알람 검색
        NoSuchElementException e1 = assertThrows(NoSuchElementException.class,
                () -> (scheduleSelectRepository.findById(scheduleSelect.getScheduleSelectId())).get());

        //삭제된 스케줄 검색
        NoSuchElementException e2 = assertThrows(NoSuchElementException.class,
                () -> (scheduleRepository.findById(schedule.getScheduleId())).get());
    }

    @Test
    void 스케줄_리스트() throws Exception{
        //given
        User userA = createUser();

        User userB = User.builder()
                .userId("수근")
                .password("1111")
                .phone("010-0000-0000")
                .build();

        userRepository.save(userB);

        Schedule scheduleA = Schedule.builder()
                .user(userA) //userA
                .title("2시 30분 잠실역")
                .scheduleDate(LocalDateTime.of(2021, 7, 28, 14, 30))
                .build();

        scheduleRepository.save(scheduleA);

        Schedule scheduleB = Schedule.builder()
                .user(userB) //userB
                .title("2시 30분 잠실역")
                .scheduleDate(LocalDateTime.of(2021, 8, 28, 14, 30))
                .build();

        scheduleRepository.save(scheduleB);

        Schedule scheduleC = Schedule.builder()
                .user(userA) //userA
                .title("2시 30분 잠실역")
                .scheduleDate(LocalDateTime.of(2021, 9, 28, 14, 30))
                .build();

        scheduleRepository.save(scheduleC);

        //schedule 1, 3일전 알람 생성
        LocalDateTime before1 = scheduleC.getScheduleDate().minusDays(1).toLocalDate().atTime(12, 00);
        LocalDateTime before3 = scheduleC.getScheduleDate().minusDays(3).toLocalDate().atTime(12, 00);

        ScheduleSelect scheduleSelectA = ScheduleSelect.builder()
                .schedule(scheduleC) //schedule
                .alarmDateTime(before1) //1일전
                .selected(1)
                .build();

        scheduleSelectRepository.save(scheduleSelectA);

        ScheduleSelect scheduleSelectB = ScheduleSelect.builder()
                .schedule(scheduleC) //schedule
                .alarmDateTime(before3) //3일전
                .selected(3)
                .build();

        scheduleSelectRepository.save(scheduleSelectB);

        /**
        * 정리 - userA : scheduleA (알람x), scheduleC (1,3일전 알람) / userB : scheduleB (알람x)
         *
         * --> userA의 스케줄 리스트 출력 예정
        * */

        //when
        List<ScheduleDTO> result = scheduleService.getListOfSchedule(userA.getUserId());

        //then
        Assertions.assertThat(result.size()).isEqualTo(2);
        //scheduleADTO(1번째)의 selected 검증
        Assertions.assertThat(result.get(0).getSelected()).isEmpty();

        //scheduleCDTO(2번째)의 selected 검증
        List<Integer> selected = scheduleSelectRepository.selectedByScheduleId(scheduleC.getScheduleId());
        Assertions.assertThat(result.get(1).getSelected()).isEqualTo(selected);
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