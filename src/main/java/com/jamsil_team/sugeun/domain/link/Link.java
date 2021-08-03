package com.jamsil_team.sugeun.domain.link;

import com.jamsil_team.sugeun.domain.BaseEntity;
import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.dto.link.LinkDTO;
import lombok.*;

import javax.persistence.*;

@Getter
@ToString(exclude = {"user","folder"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Link extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long linkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @Builder.Default
    @Column(nullable = false)
    private String link = "";

    @Builder.Default
    @Column(nullable = false)
    private Boolean bookmark = false;

    public void registerBookmark(){
        this.bookmark = true;
    }

    public void cancelBookmark(){
        this.bookmark = false;
    }

    public LinkDTO toDTO(){

        LinkDTO linkDTO = LinkDTO.builder()
                .linkId(this.linkId)
                .userId(this.user.getUserId())
                .folderId(this.folder.getFolderId())
                .link(this.link)
                .bookmark(this.bookmark)
                .build();

        return linkDTO;

    }

}
