package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.timeout.Timeout;
import com.jamsil_team.sugeun.domain.timeout.TimeoutRepository;
import com.jamsil_team.sugeun.domain.timeoutSelect.TimeoutSelect;
import com.jamsil_team.sugeun.domain.timeoutSelect.TimeoutSelectRepository;
import com.jamsil_team.sugeun.dto.timeout.TimeoutDTO;
import com.jamsil_team.sugeun.dto.timeout.TimeoutResDTO;
import com.jamsil_team.sugeun.file.FileStore;
import com.jamsil_team.sugeun.file.ResultFileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Transactional
@RequiredArgsConstructor
@Service
public class TimeoutServiceImpl implements TimeoutService{

    private final TimeoutRepository timeoutRepository;
    private final TimeoutSelectRepository timeoutSelectRepository;
    private final FileStore fileStore;

    /**
     * 타임아웃 생성
     */
    @Transactional
    @Override
    public Timeout createTimeout(TimeoutDTO timeoutDTO) throws IOException {

        Map<String, Object> entityMap = timeoutDTO.toEntity();

        //timeout 저장
        Timeout timeout = (Timeout) entityMap.get("timeout");

        ResultFileStore resultFileStore = fileStore.storeFile(timeoutDTO.getImageFile());

        timeout.changeTimeoutImg(resultFileStore);

        timeoutRepository.save(timeout);

        //timeoutSelect 알람 저장
        List<TimeoutSelect> timeoutSelectList = (List<TimeoutSelect>) entityMap.get("timeoutSelectList");

        if(timeoutSelectList != null && timeoutSelectList.size() > 0){
            timeoutSelectList.forEach(scheduleSelect ->
                    timeoutSelectRepository.save(scheduleSelect));
        }

        return timeout;
    }

    /**
     * 타임아웃 정보 수정
     */
    @Transactional
    @Override
    public void modifyTimeout(TimeoutDTO timeoutDTO) {

        Timeout timeout = timeoutRepository.findById(timeoutDTO.getTimeoutId()).orElseThrow(() ->
                new IllegalStateException("존재하지 않은 타임아웃입니다."));

        //제목, 유효기간 수정
        timeout.changeTitle(timeoutDTO.getTitle());
        timeout.changeDeadline(timeoutDTO.getDeadline().toLocalDate().atTime(23,59));

        //알람 수정
        //기존 해당 알람 모두 삭제
        timeoutSelectRepository.deleteByTimoutId(timeout.getTimeoutId());

        List<Integer> selected = timeoutDTO.getSelected();

        List<TimeoutSelect> timeoutSelectList = timeoutDTO.getTimeoutSelectEntities(selected, timeout);

        //스케줄 알람 유무 확인
        if(timeoutSelectList != null && timeoutSelectList.size() > 0){
            timeoutSelectList.forEach(scheduleSelect ->
                    timeoutSelectRepository.save(scheduleSelect));
        }
    }


    /**
     * 타임아웃 이미지 수정
     */
    @Transactional
    @Override
    public void modifyTimeoutImage(Long timeoutId, MultipartFile multipartFile) throws IOException {

        Timeout timeout = timeoutRepository.findById(timeoutId).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 타임아웃입니다."));

        //서버 컴퓨터에 저장된 기존 타임아웃 사진 삭제
        fileRemove(timeout);

        ResultFileStore resultFileStore = fileStore.storeFile(multipartFile);

        //사진저장
        timeout.changeTimeoutImg(resultFileStore);

    }


    /**
     * 사용완료 기능
     */
    @Transactional
    @Override
    public void finishUse(Long timeoutId) {
        Timeout timeout = timeoutRepository.findById(timeoutId).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 타임아웃 입니다."));

        //isValid == false 변경
        timeout.isValidFalse();

        //기존 알람 삭제
        timeoutSelectRepository.deleteByTimoutId(timeoutId);

    }


    /**
     * 타임아웃 삭제
     */
    @Transactional
    @Override
    public void removeTimeout(Long timeoutId) {
        Timeout timeout = timeoutRepository.findById(timeoutId).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 타임아웃입니다."));

        //알람 삭제 -> -> 서버컴퓨터 타임아웃 사진 삭제 -> 타임아웃 삭제
        timeoutSelectRepository.deleteByTimoutId(timeoutId);

        //서버 컴퓨터에 저장된 기존 타임아웃 사진 삭제
        fileRemove(timeout);

        timeoutRepository.deleteById(timeoutId);
    }


    private void fileRemove(Timeout timeout) {

        System.out.println("==============");
        System.out.println(timeout.toString());

        //사진 등록이 되어 있는 경우
        if(!timeout.getStoreFilename().isBlank()){

            String folderPath = timeout.getFolderPath();
            String storeFilename = timeout.getStoreFilename();

            File file = new File(fileStore.getFullPath(folderPath, storeFilename));
            file.delete();

//            File thumbnail = new File(fileStore.getThumbnailFullPath(folderPath, "s_" + storeFilename));
//            thumbnail.delete();
        }
    }

    /**
     * 타임아웃 DTO 리스트
     */
    @Transactional(readOnly = true)
    @Override
    public List<TimeoutResDTO> getListOfTimeout(Long userId) {

        List<Timeout> timeoutList = timeoutRepository.getTimeoutList(userId);

        List<TimeoutResDTO> timeoutResDTOList = timeoutList.stream().map(timeout -> {

            //타임아웃 select 리스트
            List<Integer> selected = timeoutSelectRepository.selectedByTimeoutId(timeout.getTimeoutId());

            TimeoutResDTO timeoutResDTO = timeout.toResDTO();

            timeoutResDTO.setSelected(selected);

            return timeoutResDTO;
        }).collect(Collectors.toList());

        return timeoutResDTOList;
    }


}
