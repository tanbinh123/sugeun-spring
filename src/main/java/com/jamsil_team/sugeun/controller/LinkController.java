package com.jamsil_team.sugeun.controller;

import com.jamsil_team.sugeun.dto.link.LinkDTO;
import com.jamsil_team.sugeun.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class LinkController {

    private final LinkService linkService;


    /**
     *  링크 생성
     */
    @PostMapping("/users/{user-id}/folders/{folder-id}/links")
    public ResponseEntity<String> createLink(@PathVariable("user-id") Long userId,
                                             @PathVariable("folder-id") Long folderId,
                                             @RequestBody LinkDTO linkDTO){

        linkService.createLink(linkDTO);

        return new ResponseEntity<>("링크생성 완료", HttpStatus.OK);
    }


    /**
     *  링크 삭제
     */
    @DeleteMapping("/users/{user-id}/folders/{folder-id}/links/{link-id}")
    public ResponseEntity<String> removeLink(@PathVariable("user-id") Long userId,
                                             @PathVariable("folder-id") Long folderId,
                                             @PathVariable("link-id") Long linkId){

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
