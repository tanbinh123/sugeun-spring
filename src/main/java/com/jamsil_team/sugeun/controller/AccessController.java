package com.jamsil_team.sugeun.controller;

import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.dto.user.UserDTO;
import com.jamsil_team.sugeun.dto.user.UserSignupDTO;
import com.jamsil_team.sugeun.service.sms.SendSmsService;
import com.jamsil_team.sugeun.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
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
    public ResponseEntity<Long> signup(@Valid @RequestBody UserSignupDTO userSignupDTO, BindingResult bindingResult){

        User join = userService.join(userSignupDTO);

        System.out.println(join);

        return new ResponseEntity<Long>(join.getUserId(), HttpStatus.OK);
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
    @PostMapping("/find-nickname")
    public ResponseEntity<String> findUsername(@RequestBody UserDTO userDTO){

        String nickname = userService.findNickname(userDTO.getPhone());

        return new ResponseEntity<>(nickname, HttpStatus.OK);
    }

    /**
     * 아이디 체크 (비밀번호 찾기)
     */
    @GetMapping("/check-nickname")
    public ResponseEntity<Long> checkNickname(@RequestParam("nickname") String nickname){

        Long userId = userService.checkNickname(nickname);

        return new ResponseEntity<>(userId, HttpStatus.OK);
    }

    /**
     *  phone 검증 (비밀번호 찾기)
     */
    @PostMapping("/verify-phone")
    public ResponseEntity<Boolean> verifyPhone(@RequestBody UserDTO userDTO, HttpServletResponse response) throws IOException {
        Boolean result = userService.verifyPhone(userDTO.getUserId(), userDTO.getPhone());



        if(result == true){
            String phone = userDTO.getPhone().replace("-", "");

            response.sendRedirect("/api/send-sms?toNumber="+ phone);
        }

        //저장된 핸드폰 번호와 입력 받은 번호가 다를 경우
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    /**
     *  새 비밀번호 저장 (비밀번호 찾기)
     */
    @PostMapping("/new-password")
    public ResponseEntity<String> newPassword(@RequestBody UserDTO userDTO){

        userService.modifyPassword(userDTO.getUserId(), userDTO.getPassword());

        return new ResponseEntity<>("비밀번호 변경 완료", HttpStatus.OK);
    }


}
