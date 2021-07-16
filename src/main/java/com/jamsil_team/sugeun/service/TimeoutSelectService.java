package com.jamsil_team.sugeun.service;

import java.util.List;

public interface TimeoutSelectService {

    void modifyAlarm(Long timeoutId, List<Integer> selected);

}
