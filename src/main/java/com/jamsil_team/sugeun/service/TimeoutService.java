package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.timeout.Timeout;
import com.jamsil_team.sugeun.dto.TimeoutDTO;

public interface TimeoutService {

    Timeout createTimeout(TimeoutDTO timeoutDTO);

}
