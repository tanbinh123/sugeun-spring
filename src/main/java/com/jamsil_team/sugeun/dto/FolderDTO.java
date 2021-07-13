package com.jamsil_team.sugeun.dto;

import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.domain.folder.FolderType;
import com.jamsil_team.sugeun.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FolderDTO {

    private Long folderId;

    private String folderName;

    private String userId;

    @Enumerated(value = EnumType.STRING)
    private FolderType type;

    //TODO 2021.07.10.- 사진 저장 아직 정하지 x
    private String filePath;

    private String fileName;

    private String uuid;

    private Long parentFolderId;


    public Folder toEntity(){

        Folder folder = Folder.builder()
                .folderName(this.folderName)
                .user(User.builder().userId(this.userId).build())
                .type(this.type)
                .Folder(Folder.builder().folderId(this.parentFolderId).build())
                .build();

        return folder;
    }
}
