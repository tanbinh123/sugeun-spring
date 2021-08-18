package com.jamsil_team.sugeun.controller;

import com.jamsil_team.sugeun.dto.timeout.TimeoutDTO;
import com.jamsil_team.sugeun.dto.timeout.TimeoutResDTO;
import com.jamsil_team.sugeun.service.TimeoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class TimeoutController {

    private final TimeoutService timeoutService;

    /**
     *  타임아웃 목록
     */
    @GetMapping("/users/{user-id}/timeouts")
    public ResponseEntity<List<TimeoutResDTO>> timeoutList(@PathVariable("user-id") Long userId){

        List<TimeoutResDTO> timeoutResDTOList = timeoutService.getListOfTimeout(userId);

        return new ResponseEntity<>(timeoutResDTOList, HttpStatus.OK);
    }

    /**
     *  타임아웃 생성
     */
    @PostMapping("/users/{user-id}/timeouts")
    public ResponseEntity<String> createTimeout(@PathVariable("user-id") Long userId,
                                                TimeoutDTO timeoutDTO) throws IOException {

        timeoutService.createTimeout(timeoutDTO);

        return new ResponseEntity<>("타임아웃 생성 완료", HttpStatus.OK);
    }

    /**
     *  타임아웃 변경
     */
    @PatchMapping("/users/{user-id}/timeouts/{timeout-id}")
    public ResponseEntity<String> modifyTimeout(@PathVariable("user-id") Long userId,
                                                @PathVariable("timeout-id") Long timeoutId,
                                                TimeoutDTO timeoutDTO) throws IOException {

        //이미지 변경일 경우
        if(timeoutDTO.getImageFile() != null){
            timeoutService.modifyTimeoutImage(timeoutId, timeoutDTO.getImageFile());
            return new ResponseEntity<>("타임아웃 이미지 변경 완료", HttpStatus.OK);
        }

        //정보 변경일 경우
        timeoutService.modifyTimeout(timeoutDTO);

        return new ResponseEntity<>("타임아웃 정보 변경 완료", HttpStatus.OK);
    }

    /**
     *  타임아웃 삭제
     */
    @DeleteMapping("/users/{user-id}/timeouts/{timeout-id}")
    public ResponseEntity<String> removeTimeout(@PathVariable("user-id") Long userId,
                                                @PathVariable("timeout-id") Long timeoutId){

        timeoutService.removeTimeout(timeoutId);

        return new ResponseEntity<>("타임아웃 삭제 완료", HttpStatus.OK);
    }

    /**
     *  타임아웃 사용완료
     */
    @PatchMapping("timeouts/{timeout-id}/valid")
    public ResponseEntity<String> modifyIsValid(@PathVariable("timeout-id") Long timeoutId){

        timeoutService.finishUse(timeoutId);

        return new ResponseEntity<>("타임아웃 사용 완료", HttpStatus.OK);
    }

}
