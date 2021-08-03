package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.dto.user.BookmarkDTO;
import com.jamsil_team.sugeun.dto.user.UserSignupDTO;
import com.jamsil_team.sugeun.dto.user.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {

    Boolean isDuplicateNickname(String userId);

    User join(UserSignupDTO signUpDTOUser);

    void UpdateDeviceToken(String userId, String deviceToken);

    void modifyUserImg(String userId, MultipartFile multipartFile) throws IOException;

    //void modifyUserId(String userId, String updateUserId);

    void modifyPassword(String userId, String password);

    UserDTO getUser(String userId) throws IOException;

    void modifyAlarm(String userId);

    BookmarkDTO getListOfBookmark(String userId);
}
