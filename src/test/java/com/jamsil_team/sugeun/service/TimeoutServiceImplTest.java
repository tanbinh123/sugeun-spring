package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.timeout.Timeout;
import com.jamsil_team.sugeun.domain.timeout.TimeoutRepository;
import com.jamsil_team.sugeun.domain.timeoutSelect.TimeoutSelect;
import com.jamsil_team.sugeun.domain.timeoutSelect.TimeoutSelectRepository;
import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.domain.user.UserRepository;
import com.jamsil_team.sugeun.dto.timeout.TimeoutDTO;
import com.jamsil_team.sugeun.dto.timeout.TimeoutResDTO;
import com.jamsil_team.sugeun.service.timeout.TimeoutService;
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
class TimeoutServiceImplTest {

    @Autowired
    TimeoutService timeoutService;
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
                .deadline(LocalDateTime.of(2021, 8, 11, 00, 59))
                .selected(selected)
                .build();


        //when
        Timeout timeout = timeoutService.createTimeout(timeoutDTO);

        //then
        //timeout 검증
        Assertions.assertThat(timeout.getTimeoutId()).isNotNull();
        Assertions.assertThat(timeout.getDeadline()).isEqualTo(LocalDateTime.of(2021, 8, 11, 23, 59));
        Assertions.assertThat(timeout.getIsValid()).isTrue();

        //timeoutSelect 검증
        List<TimeoutSelect> result = timeoutSelectRepository.findByUserId(timeout.getUser().getUserId());

        //selected.size() == 2
        Assertions.assertThat(result.size()).isEqualTo(2);

        //마감일: 21.08.11 23:59 -> selected.get(0) == 1 -> 알람일: 21.08.10 12:00
        Assertions.assertThat(result.get(0).getAlarmDateTime())
                .isEqualTo(LocalDateTime.of(2021,8,10,12,00));

        //마감일: 21.08.11 23:59 -> selected.get(1) == 3 -> 알람일: 21.08.08 12:00
        Assertions.assertThat(result.get(1).getAlarmDateTime())
                .isEqualTo(LocalDateTime.of(2021,8,8,12,00));

    }

    @Test
    void 타임아웃생성_알람선택x() throws Exception{
        //given
        User user = createUser();

        TimeoutDTO timeoutDTO = TimeoutDTO.builder()
                .userId(user.getUserId())
                .title("스타벅스 아메리카노")
                .deadline(LocalDateTime.of(2021, 8, 11, 00, 00))
                .build();


        //when
        Timeout timeout = timeoutService.createTimeout(timeoutDTO);

        //then
        //timeout 검증
        Assertions.assertThat(timeout.getTimeoutId()).isNotNull();
        Assertions.assertThat(timeout.getDeadline()).isEqualTo(LocalDateTime.of(2021, 8, 11, 23, 59));
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
                .deadline(LocalDateTime.of(2021, 8, 11, 00, 00))
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

    @Test
    void 타임아웃_수정() throws Exception{
        //given
        User user = createUser();

        //타임아웃 생성
        Timeout timeout = Timeout.builder()
                .user(user)
                .title("스타벅스 아메리카노")
                .deadline(LocalDateTime.of(2021, 8, 11, 00, 59))
                .build();

        timeoutRepository.save(timeout);

        //timeout 1,3일전 알람 생성
        LocalDateTime before1 = timeout.getDeadline().minusDays(1).toLocalDate().atTime(12, 00);
        LocalDateTime before3 = timeout.getDeadline().minusDays(3).toLocalDate().atTime(12, 00);

        TimeoutSelect timeoutSelectA = TimeoutSelect.builder()
                .timeout(timeout) //timeout
                .alarmDateTime(before1) //1일전
                .selected(1)
                .build();

        timeoutSelectRepository.save(timeoutSelectA);

        TimeoutSelect timeoutSelectB = TimeoutSelect.builder()
                .timeout(timeout) //timeout
                .alarmDateTime(before3) //3일전
                .selected(3)
                .build();

        timeoutSelectRepository.save(timeoutSelectB);

        TimeoutDTO timeoutDTO = TimeoutDTO.builder()
                .timeoutId(timeout.getTimeoutId())
                .userId(user.getUserId())
                .title("스타벅스 아메리카노")
                .deadline(LocalDateTime.of(2021, 8, 10, 00, 00))//11일 -> 10일
                .build();//알람 선택 x

        //제목 "스타벅스 아메리카노", 스케줄 날짜 "2021/08/10 23:59", 알람일 x
        //when
        timeoutService.modifyTimeout(timeoutDTO);

        //then
        //timeout 변경 검증
        Assertions.assertThat(timeout.getUser()).isEqualTo(user);
        Assertions.assertThat(timeout.getTitle()).isEqualTo(timeoutDTO.getTitle());
        Assertions.assertThat(timeout.getDeadline()).isEqualTo(LocalDateTime.of(2021, 8, 10, 23, 59));
        Assertions.assertThat(timeout.getIsValid()).isTrue();

        //timeoutSelect 변경 검증
        Long alarm = timeoutSelectRepository.alarmCountByTimeoutId(timeout.getTimeoutId());

        Assertions.assertThat(alarm).isEqualTo(0);
    }

    @Test
    void 타임아웃_삭제() throws Exception{
        //given
        User user = createUser();

        //타임아웃 생성
        Timeout timeout = Timeout.builder()
                .user(user)
                .title("스타벅스 아메리카노")
                .deadline(LocalDateTime.of(2021, 8, 11, 23, 59))
                .storeFilename("dfasdfas")
                .build();

        timeoutRepository.save(timeout);

        //timeout 1일전 알람 생성
        LocalDateTime before1 = timeout.getDeadline().minusDays(1).toLocalDate().atTime(12, 00);

        TimeoutSelect timeoutSelect = TimeoutSelect.builder()
                .timeout(timeout) //timeout
                .alarmDateTime(before1) //1일전
                .selected(1)
                .build();

        timeoutSelectRepository.save(timeoutSelect);

        //when
        timeoutService.removeTimeout(timeout.getTimeoutId());

        //then
        //삭제된 타임아웃의 알람 검색
        NoSuchElementException e1 = assertThrows(NoSuchElementException.class,
                () -> (timeoutSelectRepository.findById(timeoutSelect.getTimeoutSelectId())).get());

        //삭제된 스케줄 검색
        NoSuchElementException e2 = assertThrows(NoSuchElementException.class,
                () -> (timeoutRepository.findById(timeout.getTimeoutId())).get());
    }

    @Test
    void 타임아웃_리스트() throws Exception{
        //given
        User user = createUser();

        Timeout timeoutA = Timeout.builder()
                .title("나")
                .user(user)
                .deadline(LocalDateTime.of(2021, 8, 01, 23, 59))
                .isValid(true)
                .build();

        timeoutRepository.save(timeoutA);

        Timeout timeoutB = Timeout.builder()
                .title("가")
                .user(user)
                .deadline(LocalDateTime.of(2021, 9, 01, 23, 59))
                .isValid(false)
                .build();

        timeoutRepository.save(timeoutB);

        Timeout timeoutC = Timeout.builder()
                .title("다")
                .user(user)
                .deadline(LocalDateTime.of(2021, 10, 01, 23, 59))
                .isValid(true)
                .build();

        timeoutRepository.save(timeoutC);

        //timeoutC 의 1,3일전 알람 생성
        //timeout 1,3일전 알람 생성
        LocalDateTime before1 = timeoutC.getDeadline().minusDays(1).toLocalDate().atTime(12, 00);
        LocalDateTime before3 = timeoutC.getDeadline().minusDays(3).toLocalDate().atTime(12, 00);

        TimeoutSelect timeoutSelectA = TimeoutSelect.builder()
                .timeout(timeoutC) //timeout
                .alarmDateTime(before1) //1일전
                .selected(1)
                .build();

        timeoutSelectRepository.save(timeoutSelectA);

        TimeoutSelect timeoutSelectB = TimeoutSelect.builder()
                .timeout(timeoutC) //timeout
                .alarmDateTime(before3) //3일전
                .selected(3)
                .build();

        timeoutSelectRepository.save(timeoutSelectB);

        //when
        List<TimeoutResDTO> result = timeoutService.getListOfTimeout(user.getUserId());

        //then
        //timeoutBDTO(3번째) 가 제일 뒤 순서
        Assertions.assertThat(result.get(2).getTimeoutId()).isEqualTo(timeoutB.getTimeoutId());
        Assertions.assertThat(result.get(2).getIsValid()).isFalse();

        //timeoutCDTO(2번째) 의 selected 검증 (List.of(1,3))
        List<Integer> selected = timeoutSelectRepository.selectedByTimeoutId(timeoutC.getTimeoutId());
        Assertions.assertThat(result.get(1).getSelected()).isEqualTo(selected);

        //timeoutADTO(1번째) 의 selected 검증 (빈리스트)
        Assertions.assertThat(result.get(0).getSelected()).isEmpty();


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