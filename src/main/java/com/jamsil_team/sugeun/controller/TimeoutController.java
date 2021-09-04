package com.jamsil_team.sugeun.controller;

import com.jamsil_team.sugeun.domain.timeout.Timeout;
import com.jamsil_team.sugeun.dto.timeout.TimeoutDTO;
import com.jamsil_team.sugeun.dto.timeout.TimeoutResDTO;
import com.jamsil_team.sugeun.security.dto.AuthUserDTO;
import com.jamsil_team.sugeun.service.timeout.TimeoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<List<TimeoutResDTO>> timeoutList(@PathVariable("user-id") Long userId,
                                                           @AuthenticationPrincipal AuthUserDTO authUserDTO){

        if(!userId.equals(authUserDTO.getUser().getUserId())){
            throw new IllegalStateException("조회 권한이 없습니다.");
        }

        List<TimeoutResDTO> timeoutResDTOList = timeoutService.getListOfTimeout(userId);

        return new ResponseEntity<>(timeoutResDTOList, HttpStatus.OK);
    }

    /**
     *  타임아웃 생성
     */
    @PostMapping("/users/{user-id}/timeouts")
    public ResponseEntity<Long> createTimeout(TimeoutDTO timeoutDTO,
                                              @AuthenticationPrincipal AuthUserDTO authUserDTO) throws IOException {

        if(!timeoutDTO.getUserId().equals(authUserDTO.getUser().getUserId())){
            throw new IllegalStateException("생성 권한이 없습니다.");
        }

        Timeout timeout = timeoutService.createTimeout(timeoutDTO);

        return new ResponseEntity<>(timeout.getTimeoutId(), HttpStatus.OK);
    }

    /**
     *  타임아웃 변경
     */
    @PatchMapping("/users/{user-id}/timeouts/{timeout-id}")
    public ResponseEntity<String> modifyTimeout(@PathVariable("user-id") Long userId,
                                                @PathVariable("timeout-id") Long timeoutId,
                                                TimeoutDTO timeoutDTO,
                                                @AuthenticationPrincipal AuthUserDTO authUserDTO) throws IOException {
        if(!userId.equals(authUserDTO.getUser().getUserId())){
            throw new IllegalStateException("변경 권한이 없습니다.");
        }

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
                                                @PathVariable("timeout-id") Long timeoutId,
                                                @AuthenticationPrincipal AuthUserDTO authUserDTO){

        if(!userId.equals(authUserDTO.getUser().getUserId())){
            throw new IllegalStateException("삭제 권한이 없습니다.");
        }

        timeoutService.removeTimeout(timeoutId);

        return new ResponseEntity<>("타임아웃 삭제 완료", HttpStatus.OK);
    }

    /**
     *  타임아웃 사용완료
     */
    @PatchMapping("/timeouts/{timeout-id}/valid")
    public ResponseEntity<String> modifyIsValid(@PathVariable("timeout-id") Long timeoutId,
                                                @AuthenticationPrincipal AuthUserDTO authUserDTO){

        timeoutService.finishUse(timeoutId);

        return new ResponseEntity<>("타임아웃 사용 완료", HttpStatus.OK);
    }

   }
