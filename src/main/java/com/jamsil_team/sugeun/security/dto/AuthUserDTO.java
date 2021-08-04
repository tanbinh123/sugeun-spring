package com.jamsil_team.sugeun.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;

@Log4j2
@Getter
@Setter
@ToString
public class AuthUserDTO extends User {

    private Long userId;

    private String nickname;

    private String password;

    private String phone;

    private String deviceToken;

    private Boolean alarm;

    private String folderPath;

    private String storeFilename;


    public AuthUserDTO(String username, String password,
                       Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.nickname = username;
    }


}
