package com.jamsil_team.sugeun.dto.folder;

import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.domain.folder.FolderType;
import com.jamsil_team.sugeun.domain.phrase.Phrase;
import com.jamsil_team.sugeun.domain.user.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FolderDTO {

    private Long folderId;

    @NotBlank
    private String folderName;

    @NotBlank
    private Long userId;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private FolderType type;

    private Long parentFolderId;

    private MultipartFile imageFile;


    public Folder toEntity() {

        Folder folder;

        if (this.parentFolderId == null) {
            folder = Folder.builder()
                    .folderName(this.folderName)
                    .user(User.builder().userId(this.userId).build())
                    .type(this.type)
                    .build();

        } else {
            folder = Folder.builder()
                    .folderName(this.folderName)
                    .user(User.builder().userId(this.userId).build())
                    .type(this.type)
                    .parentFolder(Folder.builder().folderId(this.parentFolderId).build())
                    .build();
        }

        return folder;
    }
}
