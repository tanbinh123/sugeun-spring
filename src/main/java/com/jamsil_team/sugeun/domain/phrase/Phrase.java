package com.jamsil_team.sugeun.domain.phrase;

import com.jamsil_team.sugeun.domain.BaseEntity;
import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Phrase extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long phraseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @Builder.Default
    @Column(nullable = false, length = 1000)
    private String text = "";

    @Builder.Default
    @Column(nullable = false)
    private Boolean bookmark = false;
}
