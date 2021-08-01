package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.dto.BookmarkDTO;
import com.jamsil_team.sugeun.dto.SignupDTO;
import com.jamsil_team.sugeun.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    Boolean isDuplicateNickname(String userId);

    User join(SignupDTO signUpDTO);

    void UpdateDeviceToken(String userId, String deviceToken);

    void modifyUserImg(String userId, MultipartFile multipartFile) throws IOException;

    void modifyUserId(String userId, String updateUserId);

    void modifyPassword(String userId, String password);

    BookmarkDTO getListOfBookmark(String userId);
}
