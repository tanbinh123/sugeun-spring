package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.schedule.Schedule;
import com.jamsil_team.sugeun.domain.schedule.ScheduleRepository;
import com.jamsil_team.sugeun.domain.scheduleSelect.ScheduleSelect;
import com.jamsil_team.sugeun.domain.scheduleSelect.ScheduleSelectRepository;
import com.jamsil_team.sugeun.dto.schedule.ScheduleDTO;
import com.jamsil_team.sugeun.dto.schedule.ScheduleResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;
    private final ScheduleSelectRepository scheduleSelectRepository;

    /**
     * 스케줄 생성
     */
    @Transactional
    @Override
    public Schedule createSchedule(ScheduleDTO scheduleDTO) {

        Map<String, Object> entityMap = scheduleDTO.toEntity();

        //schedule 저장
        Schedule schedule = (Schedule) entityMap.get("schedule");

        scheduleRepository.save(schedule);

        //scheduleSelect 알람 저장
        List<ScheduleSelect> scheduleSelectList = (List<ScheduleSelect>) entityMap.get("scheduleSelectList");

        if(scheduleSelectList != null && scheduleSelectList.size() > 0){
            scheduleSelectList.forEach(scheduleSelect ->
                    scheduleSelectRepository.save(scheduleSelect));
        }

        return schedule;
    }


    /**
     * 스케줄 변경
     */
    @Transactional
    @Override
    public void modifySchedule(ScheduleDTO scheduleDTO) {

        Schedule schedule = scheduleRepository.findById(scheduleDTO.getScheduleId()).orElseThrow(() ->
                new IllegalStateException("존재하는 않는 스케줄 입니다."));

        //제목, 스케줄 날짜 수정
        schedule.changeTitle(scheduleDTO.getTitle());
        schedule.changeScheduleDate(scheduleDTO.getScheduleDate());

        //알람 수정
        //기존 해당 알람 모두 삭제
        scheduleSelectRepository.deleteByScheduleId(scheduleDTO.getScheduleId());


        List<Integer> selected = scheduleDTO.getSelected();

        List<ScheduleSelect> scheduleSelectList = scheduleDTO.getScheduleSelectEntities(selected, schedule);

        //스케줄 알람 유무 확인
        if(scheduleSelectList != null && scheduleSelectList.size() > 0){
            scheduleSelectList.forEach(scheduleSelect ->
                    scheduleSelectRepository.save(scheduleSelect));
        }
    }

    /**
     * 스케줄 삭제
     */
    @Transactional
    @Override
    public void removeSchedule(Long scheduleId) {
        //알람 삭제 -> 스케줄 삭제
        scheduleSelectRepository.deleteByScheduleId(scheduleId);
        scheduleRepository.deleteById(scheduleId);
    }


    /**
     * 스케줄 DTO 리스트
     */
    @Transactional(readOnly = true)
    @Override
    public List<ScheduleResDTO> getListOfSchedule(Long userId) {

        List<Schedule> scheduleList = scheduleRepository.getScheduleList(userId);

        List<ScheduleResDTO> scheduleResDTOList = scheduleList.stream().map(schedule -> {

            List<Integer> selected = scheduleSelectRepository.selectedByScheduleId(schedule.getScheduleId());

            ScheduleResDTO scheduleResDTO = schedule.toResDTO();
            scheduleResDTO.setSelected(selected);

            return scheduleResDTO;
        }).collect(Collectors.toList());

        return scheduleResDTOList;
    }
}
