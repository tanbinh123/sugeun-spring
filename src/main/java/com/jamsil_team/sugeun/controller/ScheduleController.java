package com.jamsil_team.sugeun.controller;

import com.jamsil_team.sugeun.dto.schedule.ScheduleDTO;
import com.jamsil_team.sugeun.dto.schedule.ScheduleResDTO;
import com.jamsil_team.sugeun.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users/{user-id}/schedules")
@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;


    /**
     *  스케줄 목록
     */
    @GetMapping
    public ResponseEntity<List<ScheduleResDTO>> scheduleList(@PathVariable("user-id") Long userId){

        List<ScheduleResDTO> scheduleDTOList = scheduleService.getListOfSchedule(userId);

        return new ResponseEntity<>(scheduleDTOList, HttpStatus.OK);

    }

    /**
     *  스케줄 생성
     */
    @PostMapping
    public ResponseEntity<String> createSchedule(@PathVariable("user-id") Long userId,
                                                 @RequestBody ScheduleDTO scheduleDTO){

        scheduleService.createSchedule(scheduleDTO);

        return new ResponseEntity<>("스케줄생성 완료", HttpStatus.OK);
    }


    /**
     *  스케줄 변경
     */
    @PatchMapping("{schedule-id}")
    public ResponseEntity<String> modifySchedule(@PathVariable("user-id") Long userId,
                                                 @PathVariable("schedule-id") Long scheduleId,
                                                 @RequestBody ScheduleDTO scheduleDTO){

        scheduleService.modifySchedule(scheduleDTO);

        return new ResponseEntity<>("스케줄 변경 완료", HttpStatus.OK);
    }


    /**
     *  스케줄 삭제
     */
    @DeleteMapping("{schedule-id}")
    public ResponseEntity<String> removeSchedule(@PathVariable("user-id") Long userId,
                                                 @PathVariable("schedule-id") Long scheduleId){

        scheduleService.removeSchedule(scheduleId);

        return new ResponseEntity<>("스케줄삭제 완료", HttpStatus.OK);
    }

}
