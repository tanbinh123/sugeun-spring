package com.jamsil_team.sugeun.controller;


import com.jamsil_team.sugeun.dto.user.BookmarkDTO;
import com.jamsil_team.sugeun.dto.user.UserDTO;
import com.jamsil_team.sugeun.dto.user.UserResDTO;
import com.jamsil_team.sugeun.dto.user.UserUpdateDTO;
import com.jamsil_team.sugeun.handler.exception.CustomApiException;
import com.jamsil_team.sugeun.security.dto.AuthUserDTO;
import com.jamsil_team.sugeun.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<UserResDTO> getUserProfile(@PathVariable("user-id") Long userId,
                                                     @AuthenticationPrincipal AuthUserDTO authUserDTO) throws IOException {

        if(!userId.equals(authUserDTO.getUser().getUserId())){
            throw new CustomApiException("조회 권한이 없습니다.");
        }

        UserResDTO userResDTO = userService.getUser(authUserDTO.getUser().getUserId());

        return new ResponseEntity<>(userResDTO, HttpStatus.OK);
    }

    /**
     *  기존 비밀번호 검증
     */
    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifyPassword(@RequestBody UserDTO userDTO,
                                                  @AuthenticationPrincipal AuthUserDTO authUserDTO){

        Boolean result = userService.verifyPassword(authUserDTO.getUser().getUserId(), userDTO.getPassword());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     *  회원정보 변경
     */
    @PatchMapping
    public ResponseEntity<String> modifyUser(@PathVariable("user-id") Long userId,
                                             UserUpdateDTO userUpdateDTO,
                                             @AuthenticationPrincipal AuthUserDTO authUserDTO) throws IOException {

        if(!userId.equals(authUserDTO.getUser().getUserId())){
            throw new CustomApiException("변경 권한이 없습니다.");
        }

        if(userUpdateDTO.getImageFile() != null){
            userService.modifyUserImg(authUserDTO.getUser().getUserId(), userUpdateDTO.getImageFile());
            return new ResponseEntity<>("이미지 업로드 완료", HttpStatus.OK);
        }
        if(userUpdateDTO.getUpdateNickname() != null){
            userService.modifyUserId(authUserDTO.getUser().getUserId(), userUpdateDTO.getUpdateNickname());
            return new ResponseEntity<>("아이디 변경 완료", HttpStatus.OK);
        }

        if(userUpdateDTO.getUpdatePassword() != null){

            System.out.println("--------------------------------");
            System.out.println("pathVariable: " + userId);
            System.out.println("password: " + userUpdateDTO.getUpdatePassword());
            System.out.println("authUserDTO: "+ authUserDTO.getUser().getUserId());

            userService.modifyPassword(authUserDTO.getUser().getUserId(), userUpdateDTO.getUpdatePassword());


            return new ResponseEntity<>("비밀번호 변경 완료", HttpStatus.OK);
        }

        return new ResponseEntity<>("회원정보 변경 실패", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     *  회원탈퇴
     */
    @DeleteMapping
    public ResponseEntity<String> removeUser(@PathVariable("user-id") Long userId,
                                             @AuthenticationPrincipal AuthUserDTO authUserDTO){

        if(!userId.equals(authUserDTO.getUser().getUserId())){
            throw new CustomApiException("탈퇴 권한이 없습니다.");
        }

        userService.removeUser(authUserDTO.getUser().getUserId());

        return new ResponseEntity<>("회원탈퇴 완료", HttpStatus.OK);

    }

    /**
     *  북마크 조회
     */
    @GetMapping("/bookmark")
    public ResponseEntity<BookmarkDTO> getBookmark(@PathVariable("user-id") Long userId,
                                                   @AuthenticationPrincipal AuthUserDTO authUserDTO){

        if(!userId.equals(authUserDTO.getUser().getUserId())){
            throw new IllegalStateException("조회 권한이 없습니다.");
        }

        BookmarkDTO bookmarkDTO = userService.getListOfBookmark(authUserDTO.getUser().getUserId());

        return new ResponseEntity<>(bookmarkDTO, HttpStatus.OK);

    }
}
