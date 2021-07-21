package com.jamsil_team.sugeun.domain.folder;

import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.dto.FolderDTO;
import lombok.*;

import javax.persistence.*;

@Getter
@ToString(exclude = {"user","folder"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Folder {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long folderId;

    @Column(nullable = false)
    private String folderName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FolderType type;

    private String filePath;

    private String fileName;

    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_folder_id")
    private Folder folder;

    public void changeFolderName(String folderName){
        this.folderName = folderName;
    }
    
    public FolderDTO toDTO(){

        FolderDTO folderDTO;

        if(this.folder != null){
            folderDTO = FolderDTO.builder()
                    .folderId(this.folderId)
                    .folderName(this.folderName)
                    .userId(this.user.getUserId())
                    .type(this.type)
                    .parentFolderId(this.folder.folderId)
                    .build();
        }else{
            folderDTO = FolderDTO.builder()
                    .folderId(this.folderId)
                    .folderName(this.folderName)
                    .userId(this.user.getUserId())
                    .type(this.type)
                    .build();
        }


        return folderDTO;
    }

}
