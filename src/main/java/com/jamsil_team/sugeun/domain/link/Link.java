package com.jamsil_team.sugeun.domain.link;

import com.jamsil_team.sugeun.domain.BaseEntity;
import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.dto.link.LinkDTO;
import com.jamsil_team.sugeun.dto.link.LinkResDTO;
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
    @JoinColumn(name = "folder_id", nullable = false)
    private Folder folder;

    @Builder.Default
    @Column(nullable = false)
    private String title = "";

    @Builder.Default
    @Column(nullable = false)
    private String link = "";

    @Builder.Default
    @Column(nullable = false)
    private Boolean bookmark = false;

    public void changeTitle(String title){

        if(title == null){
            this.title = "";
        }

        this.title = title;
    }

    public void changeLink(String link){

        if(link== null){
            this.link = "";
        }

        this.link = link;
    }

    public void registerBookmark(){
        this.bookmark = true;
    }

    public void cancelBookmark(){
        this.bookmark = false;
    }

    public LinkResDTO toResDTO(){

        LinkResDTO linkResDTO = LinkResDTO.builder()
                .linkId(this.linkId)
                .title(this.title)
                .link(this.link)
                .bookmark(this.bookmark)
                .build();

        return linkResDTO;

    }

}
