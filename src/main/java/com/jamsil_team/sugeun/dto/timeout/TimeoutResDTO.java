package com.jamsil_team.sugeun.dto.timeout;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeoutResDTO {

    private Long timeoutId;

    private Long userId;

    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime deadline;

    private Boolean isValid;

    @Builder.Default
    private List<Integer> selected = new ArrayList<>();

    private byte[] imageData;
}
