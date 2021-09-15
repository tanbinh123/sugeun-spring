package com.jamsil_team.sugeun.controller;

import com.jamsil_team.sugeun.dto.link.LinkDTO;
import com.jamsil_team.sugeun.handler.exception.CustomApiException;
import com.jamsil_team.sugeun.security.dto.AuthUserDTO;
import com.jamsil_team.sugeun.service.link.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
public class LinkController {

    private final LinkService linkService;


    /**
     *  링크 생성
     */
    @PostMapping("/users/{user-id}/folders/{folder-id}/links")
    public ResponseEntity<String> createLink(@Valid @RequestBody LinkDTO linkDTO, BindingResult bindingResult,
                                             @AuthenticationPrincipal AuthUserDTO authUserDTO){

        if(!linkDTO.getUserId().equals(authUserDTO.getUser().getUserId())){
            throw new CustomApiException("조회 권한이 없습니다.");
        }

        linkService.createLink(linkDTO);

        return new ResponseEntity<>("링크생성 완료", HttpStatus.OK);
    }


    /**
     *  링크 수정
     */
    @PutMapping("/users/{user-id}/folders/{folder-id}/links/{link-id}")
    public ResponseEntity<String> modifyLink(@PathVariable("user-id") Long userId,
                                             @RequestBody LinkDTO linkDTO,
                                             @AuthenticationPrincipal AuthUserDTO authUserDTO){

        if(!userId.equals(authUserDTO.getUser().getUserId())){
            throw new CustomApiException("수정 권한이 없습니다.");
        }

        linkService.modifyLink(linkDTO);

        return new ResponseEntity<>("링크수정 완료", HttpStatus.OK);

    }


    /**
     *  링크 삭제
     */
    @DeleteMapping("/users/{user-id}/folders/{folder-id}/links/{link-id}")
    public ResponseEntity<String> removeLink(@PathVariable("user-id") Long userId,
                                             @PathVariable("link-id") Long linkId,
                                             @AuthenticationPrincipal AuthUserDTO authUserDTO){

        if(!userId.equals(authUserDTO.getUser().getUserId())){
            throw new CustomApiException("삭제 권한이 없습니다.");
        }

        linkService.removeLink(linkId);

        return new ResponseEntity<>("링크삭제 완료", HttpStatus.OK);

    }

    /**
     *  링크 북마크 변경
     */
    @PatchMapping("/links/{link-id}/bookmark")
    public ResponseEntity<String> modifyLink(@PathVariable("link-id") Long linkId){


        linkService.modifyBookmark(linkId);

        return new ResponseEntity<>("북마크 변경 완료", HttpStatus.OK);
    }


}
