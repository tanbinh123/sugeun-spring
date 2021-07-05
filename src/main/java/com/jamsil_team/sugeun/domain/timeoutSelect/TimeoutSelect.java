package com.jamsil_team.sugeun.domain.timeoutSelect;

import com.jamsil_team.sugeun.domain.timeout.Timeout;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TimeoutSelect {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timeoutSelectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeout_id", nullable = false)
    private Timeout timeout;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime alarmDateTime;

    @Column(nullable = false)
    private int selected;
}
