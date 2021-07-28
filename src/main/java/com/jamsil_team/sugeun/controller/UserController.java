package com.jamsil_team.sugeun.controller;


import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.dto.SignupDTO;
import com.jamsil_team.sugeun.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users/{user-id}")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;






}
