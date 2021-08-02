package com.jamsil_team.sugeun.controller;


import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.dto.BookmarkDTO;
import com.jamsil_team.sugeun.dto.SignupDTO;
import com.jamsil_team.sugeun.dto.UserDTO;
import com.jamsil_team.sugeun.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users/{user-id}")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable("user-id") String userId){

        UserDTO userDTO = userService.getUser(userId);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    /*
    @PatchMapping("/image")
    public ResponseEntity<String> modifyUserImg(@PathVariable("user-id") String userId){


    }*/

    @PatchMapping("/userId")
    public ResponseEntity<String> modifyUserId(@PathVariable("user-id") String userId,
                                               @ModelAttribute UserDTO userDTO){

        userService.modifyUserId(userId, userDTO.getUserId());

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PatchMapping("/password")
    public ResponseEntity<String> modifyUserPassword(@PathVariable("user-id") String userId,
                                                     @ModelAttribute UserDTO userDTO){

        userService.modifyPassword(userId, userDTO.getPassword());

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }



    @GetMapping("/bookmark")
    public ResponseEntity<BookmarkDTO> getBookmark(@PathVariable("user-id") String userId){

        BookmarkDTO bookmarkDTO = userService.getListOfBookmark(userId);

        return new ResponseEntity<>(bookmarkDTO, HttpStatus.OK);

    }






}
