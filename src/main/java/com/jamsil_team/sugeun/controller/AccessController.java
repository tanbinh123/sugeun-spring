package com.jamsil_team.sugeun.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.dto.user.UserDTO;
import com.jamsil_team.sugeun.dto.user.UserSignupDTO;
import com.jamsil_team.sugeun.service.FolderService;
import com.jamsil_team.sugeun.service.SendSmsService;
import com.jamsil_team.sugeun.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;


@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class AccessController {

    private final SendSmsService sendSmsService;
    private final UserService userService;

    /**
     * sms 인증번호 보내기
     */
    @GetMapping("/send-sms")
    public ResponseEntity<String> sendSms(@RequestParam("toNumber") String toNumber){


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

    /**
     * 아이디 중복확인
     */
    @PostMapping("/duplicate")
    public ResponseEntity<Boolean> checkDuplicate(@RequestBody UserDTO userDTO){

        Boolean result = userService.isDuplicateNickname(userDTO.getNickname());

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<Long> signup(@RequestBody UserSignupDTO userSignupDTO){

        User join = userService.join(userSignupDTO);

        System.out.println(join);

        return new ResponseEntity<>(join.getUserId(), HttpStatus.OK);
    }


    /**
     * 로그인
     */
    @PostMapping("/login")
    public void login(){

    }


    /**
     * 아이디 찾기
     */


}
