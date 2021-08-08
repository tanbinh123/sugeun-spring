package com.jamsil_team.sugeun.controller;

import com.jamsil_team.sugeun.dto.schedule.ScheduleResDTO;
import com.jamsil_team.sugeun.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/users/{user-id}/schedules")
@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<ScheduleResDTO>> scheduleList(@PathVariable("user-id") Long userId){

        List<ScheduleResDTO> scheduleDTOList = scheduleService.getListOfSchedule(userId);

        return new ResponseEntity<>(scheduleDTOList, HttpStatus.OK);

    }

}
