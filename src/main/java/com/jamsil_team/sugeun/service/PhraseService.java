package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.dto.PhraseDTO;

public interface PhraseService {

    Long createPhrase(PhraseDTO phraseDTO);

    void modifyPhrase(PhraseDTO phraseDTO);

    void removePhrase(Long phraseId);
}
