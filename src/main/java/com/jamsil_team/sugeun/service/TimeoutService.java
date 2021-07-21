package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.timeout.Timeout;
import com.jamsil_team.sugeun.dto.TimeoutDTO;

import java.util.List;

public interface TimeoutService {

    Timeout createTimeout(TimeoutDTO timeoutDTO);

    void finishUse(Long timeoutId);

    //TODO 2021.07.18 - 수정정

   void removeTimeout(Long timeoutId);


}
