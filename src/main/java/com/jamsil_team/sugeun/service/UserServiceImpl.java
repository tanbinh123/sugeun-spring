package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.domain.user.UserRepository;
import com.jamsil_team.sugeun.dto.SignUpDTO;
import com.jamsil_team.sugeun.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 중복확인
     */
    @Transactional(readOnly = true)
    @Override
    public Boolean isDuplicateNickname(String userId) {

        Optional<User> result = userRepository.findById(userId);

        if(result.isPresent()){
            return false;
        }

        return true;
    }

    /**
     * 회원가입
     */
    @Override
    public String join(SignUpDTO signUpDTO) {


        Optional<User> result = userRepository.findById(signUpDTO.getUserId());

        if(result.isPresent()){
            throw new IllegalStateException("이미 등록된 ID 입니다.");
        }

        String rawPassword = signUpDTO.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);
        signUpDTO.setPassword(encPassword);

        User user = signUpDTO.toEntity();


        userRepository.save(user);

        return user.getUserId();

    }

    /**
     * 로그인 시 deviceToken 갱신
     */
    @Override
    public void UpdateDeviceToken(String userId, String deviceToken) {

        Optional<User> result = userRepository.findById(userId);

        if(result.isPresent()){
            User user = result.get();
            user.changeDeviceToken(deviceToken);
        }
        else{
            throw new IllegalStateException("존재하지 않는 ID 입니다.");
        }
    }


}
