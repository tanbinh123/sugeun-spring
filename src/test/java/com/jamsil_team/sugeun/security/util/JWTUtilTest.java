package com.jamsil_team.sugeun.security.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;

class JWTUtilTest {

    private JWTUtil jwtUtil;

    @BeforeEach
    public void testBefore(){

        System.out.println("testBefore.............");
        jwtUtil = new JWTUtil();
    }

    @Test
    void testEncode() throws UnsupportedEncodingException {

        Long userId = 1L;

        String tokenStr = jwtUtil.generateToken(userId);

        System.out.println(tokenStr);
    }

    @Test
    void testValidate() throws Exception {

        Long userId = 1L;

        String tokenStr = jwtUtil.generateToken(userId);

        Thread.sleep(5000);

        Long result = jwtUtil.validateAndExtract(tokenStr.substring(7));

        System.out.println(result);

        Assertions.assertThat(result).isEqualTo(1L);
    }

}