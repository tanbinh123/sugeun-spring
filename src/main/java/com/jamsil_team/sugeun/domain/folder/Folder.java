package com.jamsil_team.sugeun.domain.folder;

import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.dto.folder.FolderDTO;
import com.jamsil_team.sugeun.dto.folder.FolderResDTO;
import com.jamsil_team.sugeun.file.ResultFileStore;
import lombok.*;

import javax.persistence.*;

@Getter
@ToString(exclude = {"user","parentFolder"})
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

    @Builder.Default
    @Column(nullable = false)
    private String folderPath = "";  //이미지 저장 폴더 경로

    @Builder.Default
    @Column(nullable = false)
    private String storeFilename ="";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_folder_id")
    private Folder parentFolder;

    public void changeFolderName(String folderName){
        this.folderName = folderName;
    }


    public void changeFolderImg(ResultFileStore resultFileStore){
        if(resultFileStore == null){
            this.folderPath = "";
            this.storeFilename = "";
        }
        else{
            this.folderPath = resultFileStore.getFolderPath();
            this.storeFilename = resultFileStore.getStoreFilename();
        }
    }
    public FolderResDTO toResDTO(){


        FolderResDTO folderResDTO = FolderResDTO.builder()
                .folderId(this.folderId)
                .folderName(this.folderName)
                .userId(this.user.getUserId())
                .type(this.type)
                .build();


        return folderResDTO;
    }

}
