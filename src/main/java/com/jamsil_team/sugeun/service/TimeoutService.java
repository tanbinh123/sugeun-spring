package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.timeout.Timeout;
import com.jamsil_team.sugeun.dto.TimeoutDTO;

import java.util.List;

public interface TimeoutService {

    Timeout createTimeout(TimeoutDTO timeoutDTO);

    void finishUse(Long timeoutId);

    void modifyTimeout(TimeoutDTO timeoutDTO);

    void removeTimeout(Long timeoutId);


}
