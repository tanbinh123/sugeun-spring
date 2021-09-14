package com.jamsil_team.sugeun.dto.user;

import com.jamsil_team.sugeun.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupDTO {

    @NotBlank
    private String nickname;

    @NotBlank
    private String password;

    @NotBlank
    private String phone;


    public User toEntity(){

        User user = User.builder()
                .nickname(nickname)
                .password(password)
                .phone(phone)
                .build();

        return user;
    }
}
