package com.jamsil_team.sugeun.domain.user;

import com.jamsil_team.sugeun.domain.userRole.UserRole;
import com.jamsil_team.sugeun.dto.UserDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Builder.Default
    private Boolean alarm = false;

    private String deviceToken;

    private String filePath;

    private String fileName;

    private String uuid;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<UserRole> roleSet = new HashSet<UserRole>(Arrays.asList(UserRole.USER));

    public void changeDeviceToken(String deviceToken){
        this.deviceToken = deviceToken;
    }

    public void saveUserImg(String filePath, String uuid, String fileName){
        this.filePath = filePath;
        this.uuid = uuid;
        this.fileName = fileName;
    }


    public UserDTO toDTO(){
        UserDTO userDTO = UserDTO.builder()
                .userId(userId)
                .password(password)
                .phone(phone)
                .alarm(alarm)
                .deviceToken(deviceToken)
                .filePath(filePath)
                .fileName(fileName)
                .uuid(uuid)
                .build();

        return userDTO;
    }
}
