package com.jamsil_team.sugeun.dto.login;


import com.jamsil_team.sugeun.dto.folder.FolderResDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResDTO {

    private Long userId;

    private String jwtToken;


}
