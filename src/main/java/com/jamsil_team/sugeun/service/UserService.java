package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.dto.user.BookmarkDTO;
import com.jamsil_team.sugeun.dto.user.UserSignupDTO;
import com.jamsil_team.sugeun.dto.user.UserResDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {

    Boolean isDuplicateNickname(String nickname);

    User join(UserSignupDTO signUpDTOUser);

    void UpdateDeviceToken(Long userId, String deviceToken);

    void modifyUserImg(Long userId, MultipartFile multipartFile) throws IOException;

    void modifyUserId(Long userId, String updateUserId);

    void modifyPassword(Long userId, String password);

    Boolean verifyPassword(Long userId, String password);

    UserResDTO getUser(Long userId) throws IOException;

    void modifyAlarm(Long userId);

    BookmarkDTO getListOfBookmark(Long userId);
}
