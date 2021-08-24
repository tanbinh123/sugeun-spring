package com.jamsil_team.sugeun.controller;

import com.jamsil_team.sugeun.dto.phrase.PhraseDTO;
import com.jamsil_team.sugeun.service.phrase.PhraseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PhraseController {

    private final PhraseService phraseService;


    /**
     *  글귀 생성
     */
    @PostMapping("/users/{user-id}/folders/{folder-id}/phrases")
    public ResponseEntity<String> createPhrase(@PathVariable("user-id") Long userId,
                                               @PathVariable("folder-id") Long folderId,
                                               @RequestBody PhraseDTO phraseDTO){

        phraseService.createPhrase(phraseDTO);

        return new ResponseEntity<>("글귀생성 완료", HttpStatus.OK);
    }

    /**
     *  글귀 TEXT 변경
     */
    @PatchMapping("/users/{user-id}/folders/{folder-id}/phrases/{phrase-id}")
    public ResponseEntity<String> modifyPhrase(@PathVariable("user-id") Long userId,
                                               @PathVariable("folder-id") Long folderId,
                                               @PathVariable("phrase-id") Long phraseId,
                                               @RequestBody PhraseDTO phraseDTO){

        phraseService.ModifyPhraseText(phraseId, phraseDTO.getText());

        return new ResponseEntity<>("글귀 TEXT 변경 완료", HttpStatus.OK);

    }

    /**
     *  글귀 삭제
     */
    @DeleteMapping("/users/{user-id}/folders/{folder-id}/phrases/{phrase-id}")
    public ResponseEntity<String> removePhrase(@PathVariable("user-id") Long userId,
                                               @PathVariable("folder-id") Long folderId,
                                               @PathVariable("phrase-id") Long phraseId){

        phraseService.removePhrase(phraseId);

        return new ResponseEntity<>("글귀삭제 완료", HttpStatus.OK);
    }

    /**
     *  글귀 북마크 변경
     */
    @PatchMapping("/phrases/{phrase-id}/bookmark")
    public ResponseEntity<String> modifyBookmark(@PathVariable("phrase-id") Long phraseId){

        phraseService.ModifyBookmark(phraseId);

        return new ResponseEntity<>("북마크 변경 완료", HttpStatus.OK);
    }
}
