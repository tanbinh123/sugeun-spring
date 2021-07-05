package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.dto.SignUpDTO;
import com.jamsil_team.sugeun.dto.UserDTO;

public interface UserService {

    Boolean isDuplicateNickname(String userId);

    String join(SignUpDTO signUpDTO);

    void UpdateDeviceToken(String userId, String deviceToken);

}
