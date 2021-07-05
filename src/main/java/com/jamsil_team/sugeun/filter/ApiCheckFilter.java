package com.jamsil_team.sugeun.filter;

import com.jamsil_team.sugeun.domain.user.UserRepository;
import com.jamsil_team.sugeun.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("REQUESTURI: " + request.getRequestURI() );

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

    private boolean checkAuthHeader(HttpServletRequest request) {

        boolean checkResult = false;

        String authorization = request.getHeader("Authorization");

        if(StringUtils.hasText(authorization)){

        }

        return checkResult;

    }
}
