package com.jamsil_team.sugeun.domain.phrase;

import com.jamsil_team.sugeun.domain.BaseEntity;
import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.domain.user.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @CreatedDate
    @Column(updatable = false)
    private LocalDate textDate;

    public void changeText(String text){
        this.text = text;
        this.textDate = LocalDate.now();
    }

    public void registerBookmark(){
        this.bookmark = true;
    }

    public void cancelBookmark(){
        this.bookmark = false;
    }
}
