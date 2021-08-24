package com.jamsil_team.sugeun.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.domain.user.UserRepository;
import com.jamsil_team.sugeun.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.json.simple.JSONObject;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;
    private String pattern;
    private JWTUtil jwtUtil;
    private ObjectMapper objectMapper;

    public ApiCheckFilter(String pattern, JWTUtil jwtUtil) {
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
        this.jwtUtil = jwtUtil;
        this.objectMapper = new ObjectMapper();
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

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


            if(checkHeader){
                filterChain.doFilter(request,response);
                return;
            }
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

        boolean checkResult = false;




        String authHeader = request.getHeader("Authorization");

        System.out.println("==========================");
        System.out.println(authHeader);


        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            log.info("Authorization exist: " + authHeader);

            Long userId = jwtUtil.validateAndExtract(authHeader.substring(7));

            log.info("userId: " + userId);
            checkResult = userId > 0L;

        }

        return checkResult;

    }
}
