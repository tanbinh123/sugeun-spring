package com.jamsil_team.sugeun.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkDTO {

    @Builder.Default
    private List<PhraseDTO> phraseDTOList = new ArrayList<>();

    @Builder.Default
    private List<LinkDTO> linkDTOList = new ArrayList<>();
}
