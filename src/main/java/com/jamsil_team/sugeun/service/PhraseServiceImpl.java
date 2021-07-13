package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.phrase.Phrase;
import com.jamsil_team.sugeun.domain.phrase.PhraseRepository;
import com.jamsil_team.sugeun.dto.PhraseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class PhraseServiceImpl implements PhraseService{

    private final PhraseRepository phraseRepository;

    /**
     * 글귀생성
     */
    @Override
    public Phrase createPhrase(PhraseDTO phraseDTO) {

        Phrase phrase = phraseDTO.toEntity();

        phraseRepository.save(phrase);

        return phrase;
    }

    /**
     * 글귀 text 변경
     */
    @Override
    public void ModifyPhraseText(Long phraseId, String text) {

        Phrase phrase = phraseRepository.findById(phraseId).orElseThrow(() ->
            new IllegalStateException("존재하지 않는 글귀입니다."));

        phrase.changeText(text);

    }

    /**
     * 글귀 북마크 기능
     */
    @Override
    public void ModifyBookmark(Long phraseId) {

        Phrase phrase = phraseRepository.findById(phraseId).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 글귀입니다."));

        if (phrase.getBookmark() == true){
            phrase.cancelBookmark();
        }else{
            phrase.registerBookmark();
        }

    }

    /**
     * 글귀삭제
     */
    @Override
    public void removePhrase(Long phraseId) {

        phraseRepository.deleteById(phraseId);

    }
}
