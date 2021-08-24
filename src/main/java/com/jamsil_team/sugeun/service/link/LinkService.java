package com.jamsil_team.sugeun.service.link;


import com.jamsil_team.sugeun.domain.link.Link;
import com.jamsil_team.sugeun.dto.link.LinkDTO;

public interface LinkService {

    Link createLink(LinkDTO linkDTO);

    void modifyLink(LinkDTO linkDTO);

    void removeLink(Long linkId);

    void modifyBookmark(Long linkId);
}
