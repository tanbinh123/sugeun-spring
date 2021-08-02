package com.jamsil_team.sugeun.dto;

import com.jamsil_team.sugeun.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String userId;

    private String password;

    private String phone;

    private Boolean alarm;

    @Builder.Default
    private String folderPath = "";

    @Builder.Default
    private String storeFilename = "";

    private MultipartFile file ;


    public User toEntity() {
        User user = User.builder()
                .userId(userId)
                .password(password)
                .phone(phone)
                .alarm(alarm)
                .folderPath(folderPath)
                .storeFilename(storeFilename)
                .build();

        return user;
    }

}
