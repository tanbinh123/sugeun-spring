package com.jamsil_team.sugeun.dto.user;

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
public class UserSignupDTO {

    @NotEmpty
    private String nickname;

    @NotEmpty
    private String password;

    @NotEmpty
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
