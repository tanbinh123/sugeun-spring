package com.jamsil_team.sugeun.dto;

import com.jamsil_team.sugeun.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDTO {

    @NotEmpty
    private String userId;

    @NotEmpty
    private String password;

    @NotEmpty
    private String phone;

    private MultipartFile file;

    public User toEntity(){

        User user = User.builder()
                .userId(userId)
                .password(password)
                .phone(phone)
                .build();

        return user;
    }
}
