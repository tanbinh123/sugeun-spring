package com.jamsil_team.sugeun.dto.user;


import com.jamsil_team.sugeun.dto.link.LinkDTO;
import com.jamsil_team.sugeun.dto.link.LinkResDTO;
import com.jamsil_team.sugeun.dto.phrase.PhraseDTO;
import com.jamsil_team.sugeun.dto.phrase.PhraseResDTO;
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
    private List<PhraseResDTO> phraseResDTOList = new ArrayList<>();

    @Builder.Default
    private List<LinkResDTO> linkResDTOList = new ArrayList<>();
}
