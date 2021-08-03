package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.timeout.Timeout;
import com.jamsil_team.sugeun.dto.timeout.TimeoutDTO;

import java.util.List;

public interface TimeoutService {

    Timeout createTimeout(TimeoutDTO timeoutDTO);

    void finishUse(Long timeoutId);

    void modifyTimeout(TimeoutDTO timeoutDTO);

    void removeTimeout(Long timeoutId);

    List<TimeoutDTO> getListOfTimeout(String userId);


}
