package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.link.Link;
import com.jamsil_team.sugeun.domain.link.LinkRepository;
import com.jamsil_team.sugeun.dto.LinkDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class LinkServiceImpl implements LinkService{

    private final LinkRepository linkRepository;

    @Override
    public Link createLink(LinkDTO linkDTO) {

        Link link = linkDTO.toEntity();

        linkRepository.save(link);

        return link;
    }

    @Override
    public void modifyBookmark(Long linkId) {

        Link link = linkRepository.findById(linkId).orElseThrow(() ->
                new IllegalStateException("존재하는 링크가 없습니다."));

        if (link.getBookmark() == true){
            link.cancelBookmark();
        }else{
            link.registerBookmark();
        }

    }

    @Override
    public void removeLink(Long linkId) {

        linkRepository.deleteById(linkId);

    }
}
