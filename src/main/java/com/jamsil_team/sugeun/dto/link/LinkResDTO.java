package com.jamsil_team.sugeun.dto.link;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkResDTO {

    private Long linkId;

    private String title;

    private String link;

    private Boolean bookmark;

}
