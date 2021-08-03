package com.jamsil_team.sugeun.dto.folder;

import com.jamsil_team.sugeun.dto.link.LinkDTO;
import com.jamsil_team.sugeun.dto.phrase.PhraseDTO;
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
public class DetailFolderDTO {

    @Builder.Default
    private List<PhraseDTO> phraseDTOList = new ArrayList<>();

    @Builder.Default
    private List<LinkDTO> linkDTOList = new ArrayList<>();

    @Builder.Default
    private List<FolderResDTO> folderResDTOList = new ArrayList<>();


}
