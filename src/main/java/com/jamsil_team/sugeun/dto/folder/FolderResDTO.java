package com.jamsil_team.sugeun.dto.folder;


import com.jamsil_team.sugeun.domain.folder.FolderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FolderResDTO {

    private Long folderId;

    private String folderName;

    private String userId;

    @Enumerated(EnumType.STRING)
    private FolderType type;

    private byte[] imageData;

    private Long parentFolderId;

}
