package com.jamsil_team.sugeun.controller;


import com.jamsil_team.sugeun.dto.BookmarkDTO;
import com.jamsil_team.sugeun.dto.UserDTO;
import com.jamsil_team.sugeun.dto.UserUpdateDTO;
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
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable("user-id") String userId){

        UserDTO userDTO = userService.getUser(userId);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }


    /**
     *  이미지 업로드
     */
    @PatchMapping("/image")
    public ResponseEntity<String> modifyUserImg(@PathVariable("user-id") String userId,
                                                @ModelAttribute UserUpdateDTO userUpdateDTO) throws IOException {

        userService.modifyUserImg(userId, userUpdateDTO.getImageFile());

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    /**
     *  아이디 변경
     */
    /*
    @PatchMapping("/userId")
    public ResponseEntity<String> modifyUserId(@PathVariable("user-id") String userId,
                                               @ModelAttribute UserUpdateDTO userUpdateDTO){

        userService.modifyUserId(userId, userUpdateDTO.getUpdateId());

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }*/

    /**
     *  비밀번호 변경
     */
    @PatchMapping("/password")
    public ResponseEntity<String> modifyUserPassword(@PathVariable("user-id") String userId,
                                                     @ModelAttribute UserUpdateDTO userUpdateDTO){

        userService.modifyPassword(userId, userUpdateDTO.getUpdatePassword());

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


    /**
     *  알림 허용 변경
     */
    @PatchMapping("/alarm")
    public ResponseEntity<String> modifyUserAlarm(@PathVariable("user-id") String userId){

        userService.modifyAlarm(userId);

        return new ResponseEntity<>("OK",HttpStatus.OK);
    }


    /**
     *  북마크 조회
     */
    @GetMapping("/bookmark")
    public ResponseEntity<BookmarkDTO> getBookmark(@PathVariable("user-id") String userId){

        BookmarkDTO bookmarkDTO = userService.getListOfBookmark(userId);

        return new ResponseEntity<>(bookmarkDTO, HttpStatus.OK);

    }






}
