package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.dto.SignUpDTO;
import com.jamsil_team.sugeun.dto.UserDTO;

public interface UserService {

    Boolean isDuplicateNickname(String userId);

    User join(SignUpDTO signUpDTO);

    void UpdateDeviceToken(String userId, String deviceToken);

    Boolean verifyPassword(String userId, String password);

    void modifyPassword(String userId, String password);
}
