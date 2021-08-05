package com.jamsil_team.sugeun.dto.phrase;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhraseResDTO {

    private Long phraseId;

    private String text;

    private Boolean bookmark;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd", timezone = "Asia/Seoul")
    private LocalDate textDate;
}
