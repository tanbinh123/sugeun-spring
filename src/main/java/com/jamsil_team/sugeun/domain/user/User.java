package com.jamsil_team.sugeun.domain.user;

import com.jamsil_team.sugeun.domain.userRole.UserRole;
import com.jamsil_team.sugeun.dto.UserDTO;
import com.jamsil_team.sugeun.file.ResultFileStore;
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

    private String folderPath;

    private String storeFilename;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<UserRole> roleSet = new HashSet<UserRole>(Arrays.asList(UserRole.USER));

    public void changeDeviceToken(String deviceToken){
        this.deviceToken = deviceToken;
    }

    public void changePassword(String password){
        this.password = password;
    }

    public void changeUserImg(ResultFileStore resultFileStore){

        if(resultFileStore == null){
            this.folderPath = "";
            this.storeFilename = "";
        }
        else{
            this.folderPath = resultFileStore.getFolderPath();
            this.storeFilename = resultFileStore.getStoreFilename();
        }
    }

    public UserDTO toDTO(){
        UserDTO userDTO = UserDTO.builder()
                .userId(userId)
                .password(password)
                .phone(phone)
                .alarm(alarm)
                .build();

        return userDTO;
    }
}
