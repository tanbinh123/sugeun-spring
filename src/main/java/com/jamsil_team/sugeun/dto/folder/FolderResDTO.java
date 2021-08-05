package com.jamsil_team.sugeun.dto.folder;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FolderResDTO {

    private Long folderId;

    private String folderName;

    private Long userId;

    private byte[] imageData;
}
