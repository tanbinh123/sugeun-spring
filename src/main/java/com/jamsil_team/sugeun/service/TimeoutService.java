package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.timeout.Timeout;
import com.jamsil_team.sugeun.dto.timeout.TimeoutDTO;
import com.jamsil_team.sugeun.dto.timeout.TimeoutResDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TimeoutService {

    Timeout createTimeout(TimeoutDTO timeoutDTO) throws IOException;

    void finishUse(Long timeoutId);

    void modifyTimeout(TimeoutDTO timeoutDTO);

    void modifyTimeoutImage(Long timeoutId, MultipartFile multipartFile) throws IOException;

    void removeTimeout(Long timeoutId);

    List<TimeoutResDTO> getListOfTimeout(Long userId);


}
