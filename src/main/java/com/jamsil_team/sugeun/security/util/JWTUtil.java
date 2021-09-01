package com.jamsil_team.sugeun.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import lombok.extern.log4j.Log4j2;

import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {

    private String secretKey = "sugeun1234";

    private long expire = 60 * 24 * 30;

    //토큰 생성
    public String generateToken(Long userId) throws UnsupportedEncodingException {

        return "Bearer " + Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"))
                .compact();
    }

    public Long validateAndExtract(String tokenStr){
       Long userId = null;

        try {
            DefaultJws defaultJws = (DefaultJws) Jwts.parser().setSigningKey(secretKey.getBytes("UTF-8"))
                    .parseClaimsJws(tokenStr);

            log.info(defaultJws);
            log.info(defaultJws.getBody().getClass());

            DefaultClaims claims = (DefaultClaims) defaultJws.getBody();

            log.info("--------------------");
            log.info("nickname: " + claims.get("userId", Long.class));

            userId = claims.get("userId", Long.class);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            userId = null;
        }

        return userId;
    }
}
