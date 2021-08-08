package com.jamsil_team.sugeun.domain.schedule;

import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.dto.schedule.ScheduleDTO;
import com.jamsil_team.sugeun.dto.schedule.ScheduleResDTO;
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
public class Schedule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    @Column(nullable = false)
    private LocalDateTime scheduleDate;

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeScheduleDate(LocalDateTime scheduleDate){
        this.scheduleDate = scheduleDate;
    }

    public ScheduleResDTO toResDTO(){

        ScheduleResDTO scheduleResDTO = ScheduleResDTO.builder()
                .scheduleId(this.scheduleId)
                .userId(this.user.getUserId())
                .title(this.title)
                .scheduleDate(this.scheduleDate)
                .build();

        return scheduleResDTO;
    }
}
