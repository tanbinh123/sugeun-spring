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
     *  회원정보 수정
     */
    @PatchMapping
    public ResponseEntity<String> modifyUser(@PathVariable("user-id") Long userId,
                                             @RequestBody UserUpdateDTO userUpdateDTO) throws IOException {

        if(userUpdateDTO.getImageFile() != null){
            userService.modifyUserImg(userId, userUpdateDTO.getImageFile());
            return new ResponseEntity<>("이미지 업로드 완료", HttpStatus.OK);
        }
        if(userUpdateDTO.getUpdateNickname() != null){
            userService.modifyUserId(userId, userUpdateDTO.getUpdateNickname());
            return new ResponseEntity<>("아이디 변경 완료", HttpStatus.OK);
        }

        if(userUpdateDTO.getUpdatePassword() != null){
            userService.modifyPassword(userId, userUpdateDTO.getUpdatePassword());
            return new ResponseEntity<>("비밀번호 변경 완료", HttpStatus.OK);
        }

        return null;
    }


    /**
     *  알림 허용 변경
     */
    @PatchMapping("/alarm")
    public ResponseEntity<String> modifyUserAlarm(@PathVariable("user-id") Long userId){

        userService.modifyAlarm(userId);

        return new ResponseEntity<>("알림허용 변경 완료",HttpStatus.OK);
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
