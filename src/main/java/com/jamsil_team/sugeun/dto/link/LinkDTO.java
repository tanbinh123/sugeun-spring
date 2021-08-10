package com.jamsil_team.sugeun.dto.link;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.domain.link.Link;
import com.jamsil_team.sugeun.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkDTO {

    private Long linkId;

    private Long userId;

    private Long folderId;

    private String title;

    private String link;

    private Boolean bookmark;

    public Link toEntity() {

        Link link;

        if (folderId == null) {
            link = Link.builder()
                    .user(User.builder().userId(this.userId).build())
                    .title(this.title)
                    .link(this.link)
                    .build();
        } else {
            link = Link.builder()
                    .user(User.builder().userId(this.userId).build())
                    .folder(Folder.builder().folderId(this.folderId).build())
                    .title(this.title)
                    .link(this.link)
                    .build();
        }

        return link;
    }
}
