package com.jamsil_team.sugeun.security;

import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.domain.user.UserRepository;
import com.jamsil_team.sugeun.security.dto.AuthUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("------------------------");
        log.info("username: " + username);

        Optional<User> result = userRepository.findByUserId(username);

        if(result.isEmpty()){
            throw new IllegalStateException("존재하지 않는 ID 입니다.");
        }

        User user = result.get();

        AuthUserDTO authUserDTO = new AuthUserDTO(user.getUserId(), user.getPassword(),
                user.getRoleSet().stream().map(role ->
                        new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toList()));

        authUserDTO.setPhone(user.getPhone());
        authUserDTO.setAlarm(user.getAlarm());
        authUserDTO.setPassword(user.getPassword());
        authUserDTO.setFileName(user.getFileName());
        authUserDTO.setFilePath(user.getFilePath());
        authUserDTO.setUuid(user.getUuid());

        System.out.println("authUserDTO = " + authUserDTO.getPassword()); //password 가 제대로 지정됐는지 확인

        return authUserDTO;
    }
}
