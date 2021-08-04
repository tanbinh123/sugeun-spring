package com.jamsil_team.sugeun.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.domain.user.UserRepository;
import com.jamsil_team.sugeun.dto.folder.FolderResDTO;
import com.jamsil_team.sugeun.dto.user.UserDTO;
import com.jamsil_team.sugeun.dto.user.UserResDTO;
import com.jamsil_team.sugeun.service.FolderService;
import com.jamsil_team.sugeun.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StreamUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    private UserRepository userRepository;
    private UserService userService;
    private FolderService folderService;
    private ObjectMapper objectMapper = new ObjectMapper();

    public ApiLoginFilter(String defaultFilterProcessesUrl, UserRepository userRepository, UserService userService, FolderService folderService) {
        super(defaultFilterProcessesUrl);
        this.userRepository = userRepository;
        this.userService =userService;
        this.folderService = folderService;
    }

    private String deviceToken;

    /**
     * 로그인 인증토큰 생성
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        log.info("----------------------ApiLoginFilter-------------------");
        log.info("attemptAuthentication");

        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        System.out.println(messageBody);

        UserDTO userDTO = objectMapper.readValue(messageBody, UserDTO.class);

        this.deviceToken = request.getHeader("Authorization");

        System.out.println(deviceToken);

        log.info("UserDTO: "+ userDTO);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDTO.getNickname(), userDTO.getPassword());

        return getAuthenticationManager().authenticate(authToken);
    }

    /**
     * 로그인 성공시
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        log.info("---------------successfulAuthentication--------------------");
        log.info("successfulAuthentication: " + authResult);

        log.info(authResult.getPrincipal());

        //로그인 시 받은 deviceToken 갱신
        String nickname = authResult.getName();

        System.out.println("---------------");
        System.out.println(nickname);


        User user = userRepository.findByNickname(nickname).orElseThrow(() ->
                new IllegalStateException("존재하지 않은 아이디입니다."));

        //로그인시 받은 token 과 기존  token 이 다를 경우 갱신
        if(user.getDeviceToken() != this.deviceToken){
            userService.UpdateDeviceToken(user.getUserId(), this.deviceToken);
        }

        //response json 값
        response.setHeader("content-type", "application/json");
        response.setCharacterEncoding("utf-8");


        List<FolderResDTO> folderResDTOList = folderService.getListOfFolder(user.getUserId(), null, null);

        String result = objectMapper.writeValueAsString(folderResDTOList);
        response.getWriter().write(result);

    }

    /**
     *  로그인 실패시
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        log.info("login fail handler....................");
        log.info(failed.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.setContentType("application/json;charset=utf-8");

        JSONObject json = new JSONObject();
        String message = failed.getMessage();
        json.put("code", "401");
        json.put("message", message);


        PrintWriter out = response.getWriter();
        out.print(json);
    }

}
