package com.jamsil_team.sugeun.dto.timeout;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jamsil_team.sugeun.domain.timeout.Timeout;
import com.jamsil_team.sugeun.domain.timeoutSelect.TimeoutSelect;
import com.jamsil_team.sugeun.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeoutDTO {

    private Long timeoutId;

    private Long userId;

    private String title;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime deadline;

    private Boolean isValid;

    @Builder.Default
    private List<Integer> selected = new ArrayList<>();

    private MultipartFile imageFile;


    public Map<String, Object> toEntity(){

        HashMap<String, Object> entityMap = new HashMap<>();

        //timeout 생성
        Timeout timeout = Timeout.builder()
                .user(User.builder().userId(this.userId).build())
                .title(this.title)
                .deadline(this.deadline.toLocalDate().atTime(23,59))
                .build();

        entityMap.put("timeout", timeout);

        List<TimeoutSelect> timeoutSelectList = getTimeoutSelectEntities(selected, timeout);

        entityMap.put("timeoutSelectList", timeoutSelectList);

        return entityMap;
    }

    public List<TimeoutSelect> getTimeoutSelectEntities(List<Integer> selected, Timeout timeout) {

        //알람선택 유무 확인
        if( this.selected != null && this.selected.size() > 0){
            List<TimeoutSelect> timeoutSelectList = selected.stream().map(integer -> {
                TimeoutSelect timeoutSelect = TimeoutSelect.builder()
                        .timeout(timeout)
                        .selected(integer)
                        .alarmDateTime(this.deadline.minusDays(integer.longValue()).toLocalDate().atTime(12,00))
                        .build();
                return timeoutSelect;
            }).collect(Collectors.toList());

            return timeoutSelectList;
        }

        return null;
    }


}
