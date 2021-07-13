package com.jamsil_team.sugeun.service;


import com.jamsil_team.sugeun.domain.link.Link;
import com.jamsil_team.sugeun.dto.LinkDTO;

public interface LinkService {

    Link createLink(LinkDTO linkDTO);

    void modifyBookmark(Long linkId);

    void removeLink(Long linkId);

}
