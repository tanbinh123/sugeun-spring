package com.jamsil_team.sugeun.controller;

import com.jamsil_team.sugeun.dto.timeout.TimeoutDTO;
import com.jamsil_team.sugeun.dto.timeout.TimeoutResDTO;
import com.jamsil_team.sugeun.service.TimeoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/users/{user-id}/timeouts")
@RequiredArgsConstructor
@RestController
public class TimeoutController {

    private final TimeoutService timeoutService;

    /**
     *  타임아웃 목록
     */
    @GetMapping
    public ResponseEntity<String> timeoutList(@PathVariable("user-id") Long userId){

        List<TimeoutResDTO> timeoutResDTOList = timeoutService.getListOfTimeout(userId);

        return null;
    }

}
