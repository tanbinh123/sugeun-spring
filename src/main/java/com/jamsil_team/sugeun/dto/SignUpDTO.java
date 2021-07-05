package com.jamsil_team.sugeun.dto;

import com.jamsil_team.sugeun.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public User toEntity(){

        User user = User.builder()
                .userId(userId)
                .password(password)
                .phone(phone)
                .build();

        return user;
    }
}
