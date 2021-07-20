package com.jamsil_team.sugeun.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.domain.user.UserRepository;
import com.jamsil_team.sugeun.dto.UserDTO;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;
    private String pattern;
    private UserRepository userRepository;
    private ObjectMapper objectMapper;

    public ApiCheckFilter(String pattern, UserRepository userRepository) {
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
        this.userRepository = userRepository;
        this.objectMapper = new ObjectMapper();
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println(request.getPathTranslated());
        System.out.println(request.getServletPath());
        System.out.println(request.getPathInfo());
        log.info("REQUESTURI: " + request.getRequestURI() );
        log.info(antPathMatcher.match(pattern, request.getRequestURI()));

        /**
         * /api/login, api/signUp 이면 바로 다음 필터로 패스
         */
        if(antPathMatcher.match(pattern, request.getRequestURI())){
            filterChain.doFilter(request,response);
            return ;
        }

        /**
         * this.pattern 이외의 모든 호출에 실행 (deviceToken 검증)
         */
        else{

            boolean checkHeader = checkAuthHeader(request);

            //deviceToken 같을 경우
            if(checkHeader){
                filterChain.doFilter(request,response);
                return;
            }
            //deviceToken 다를 경우우
            else{
               response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "FAIL CHECK API TOKEN";
                json.put("code", "403");
                json.put("message", message);

                PrintWriter out = response.getWriter();
                out.println(json);
                return;
            }

        }
    }


    private boolean checkAuthHeader(HttpServletRequest request) {

        String deviceToken = request.getHeader("Authorization");
        String userId = request.getHeader("userID");

        // DB에 저장된 deviceToken 가져오기
        User user = userRepository.findByUserId(userId).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 ID 입니다."));

        log.info("UserDTO.getDeviceToken(): " + deviceToken);
        log.info("User.getDeviceToken(): " + user.getDeviceToken());

        //request 로 받은 deviceToken 과 DB에 저장된 deviceToken 동일 체크
        boolean checkResult = deviceToken.equals(user.getDeviceToken());

        System.out.println(checkResult);

        return checkResult;

    }
}
