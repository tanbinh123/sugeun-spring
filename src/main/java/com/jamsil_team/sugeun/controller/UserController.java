package com.jamsil_team.sugeun.controller;


import com.jamsil_team.sugeun.dto.user.BookmarkDTO;
import com.jamsil_team.sugeun.dto.user.UserResDTO;
import com.jamsil_team.sugeun.dto.user.UserUpdateDTO;
import com.jamsil_team.sugeun.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/users/{user-id}")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    /**
     *  프로필 조회
     */
    @GetMapping
    public ResponseEntity<UserResDTO> getUserProfile(@PathVariable("user-id") Long userId) throws IOException {

        UserResDTO userResDTO = userService.getUser(userId);

        return new ResponseEntity<>(userResDTO, HttpStatus.OK);
    }


    /**
     *  이미지 업로드
     */
    @PatchMapping("/image")
    public ResponseEntity<String> modifyUserImg(@PathVariable("user-id") Long userId,
                                                @RequestBody UserUpdateDTO userUpdateDTO) throws IOException {

        userService.modifyUserImg(userId, userUpdateDTO.getImageFile());

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    /**
     *  아이디 변경
     */
    /*
    @PatchMapping("/userId")
    public ResponseEntity<String> modifyUserId(@PathVariable("user-id") Long userId,
                                               @ModelAttribute UserUpdateDTO userUpdateDTO){

        userService.modifyUserId(userId, userUpdateDTO.getUpdateId());

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }*/

    /**
     *  비밀번호 변경
     */
    @PatchMapping("/password")
    public ResponseEntity<String> modifyUserPassword(@PathVariable("user-id") Long userId,
                                                     @RequestBody UserUpdateDTO userUpdateDTO){

        userService.modifyPassword(userId, userUpdateDTO.getUpdatePassword());

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


    /**
     *  알림 허용 변경
     */
    @PatchMapping("/alarm")
    public ResponseEntity<String> modifyUserAlarm(@PathVariable("user-id") Long userId){

        userService.modifyAlarm(userId);

        return new ResponseEntity<>("OK",HttpStatus.OK);
    }


    /**
     *  북마크 조회
     */
    @GetMapping("/bookmark")
    public ResponseEntity<BookmarkDTO> getBookmark(@PathVariable("user-id") Long userId){

        BookmarkDTO bookmarkDTO = userService.getListOfBookmark(userId);

        return new ResponseEntity<>(bookmarkDTO, HttpStatus.OK);

    }
}
