package com.jamsil_team.sugeun.domain.scheduleSelect;

import com.jamsil_team.sugeun.domain.schedule.Schedule;
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
public class ScheduleSelect {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleSelectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime alarmDateTime;

    @Column(nullable = false)
    private int selected;
}
