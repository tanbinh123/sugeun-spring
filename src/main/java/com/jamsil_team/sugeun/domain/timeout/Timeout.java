package com.jamsil_team.sugeun.domain.timeout;

import com.jamsil_team.sugeun.domain.user.User;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString(exclude = {"user"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Timeout {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timeoutId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    @Column(nullable = false)
    private LocalDateTime deadline;

    @Builder.Default
    private Boolean isValid = true;

    private String filePath;

    private String fileName;

    private String uuid;

    public void isValidFalse(){
        this.isValid = false;
    }

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeDeadline(LocalDateTime deadline){
        this.deadline = deadline;
    }

}
