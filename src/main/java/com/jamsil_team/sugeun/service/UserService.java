package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.dto.SignupDTO;

public interface UserService {

    Boolean isDuplicateNickname(String userId);

    User join(SignupDTO signUpDTO);

    void UpdateDeviceToken(String userId, String deviceToken);

    Boolean verifyPassword(String userId, String password);

    void modifyPassword(String userId, String password);
}
