package com.jamsil_team.sugeun.dto;

import com.jamsil_team.sugeun.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String userId;

    private String password;

    private String phone;

    private Boolean alarm;

    private String deviceToken;

    private String filePath;

    private String fileName;

    private String uuid;

    public User toEntity() {
        User user = User.builder()
                .userId(userId)
                .password(password)
                .phone(phone)
                .alarm(alarm)
                .deviceToken(deviceToken)
                .filePath(filePath)
                .fileName(fileName)
                .uuid(uuid)
                .build();

        return user;
    }

}
