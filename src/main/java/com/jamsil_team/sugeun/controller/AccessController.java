package com.jamsil_team.sugeun.controller;

import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.dto.user.UserSignupDTO;
import com.jamsil_team.sugeun.service.FolderService;
import com.jamsil_team.sugeun.service.SendSmsService;
import com.jamsil_team.sugeun.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;


@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class AccessController {

    private final SendSmsService sendSmsService;
    private final UserService userService;
    private final FolderService folderService;



    @GetMapping("/send-sms")
    public ResponseEntity<String> sendSms(@RequestBody String toNumber){


        System.out.println(toNumber);
        System.out.println(toNumber.getClass());

        //4자리 인증번호 생성
        Random rand  = new Random();
        String certifyNumber = "";
        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            certifyNumber+=ran;
        }


        System.out.println(certifyNumber);

        sendSmsService.sendSms(toNumber, certifyNumber);


        return new ResponseEntity<>(certifyNumber, HttpStatus.OK);

    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserSignupDTO signUpDTOUser){

        User join = userService.join(signUpDTOUser);

        System.out.println(join);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/login")
    public void login(){

    }





}
