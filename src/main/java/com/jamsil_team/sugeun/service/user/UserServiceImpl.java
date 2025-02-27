package com.jamsil_team.sugeun.service.user;

import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.domain.folder.FolderRepository;
import com.jamsil_team.sugeun.domain.link.Link;
import com.jamsil_team.sugeun.domain.link.LinkRepository;
import com.jamsil_team.sugeun.domain.phrase.Phrase;
import com.jamsil_team.sugeun.domain.phrase.PhraseRepository;
import com.jamsil_team.sugeun.domain.schedule.Schedule;
import com.jamsil_team.sugeun.domain.schedule.ScheduleRepository;
import com.jamsil_team.sugeun.domain.timeout.Timeout;
import com.jamsil_team.sugeun.domain.timeout.TimeoutRepository;
import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.domain.user.UserRepository;

import com.jamsil_team.sugeun.dto.link.LinkResDTO;
import com.jamsil_team.sugeun.dto.phrase.PhraseResDTO;
import com.jamsil_team.sugeun.dto.user.BookmarkDTO;
import com.jamsil_team.sugeun.dto.user.UserResDTO;
import com.jamsil_team.sugeun.dto.user.UserSignupDTO;
import com.jamsil_team.sugeun.file.FileStore;
import com.jamsil_team.sugeun.file.ResultFileStore;
import com.jamsil_team.sugeun.handler.exception.CustomApiException;
import com.jamsil_team.sugeun.service.folder.FolderService;
import com.jamsil_team.sugeun.service.schedule.ScheduleService;
import com.jamsil_team.sugeun.service.timeout.TimeoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStore fileStore;
    private final PhraseRepository phraseRepository;
    private final LinkRepository linkRepository;
    private final TimeoutService timeoutService;
    private final TimeoutRepository timeoutRepository;
    private final ScheduleService scheduleService;
    private final ScheduleRepository scheduleRepository;
    private final FolderService folderService;
    private final FolderRepository folderRepository;

    /**
     * 아이디 중복확인
     */
    @Transactional(readOnly = true)
    @Override
    public Boolean isDuplicateNickname(String nickname) {

        Optional<User> result = userRepository.findByNickname(nickname);

        if(result.isPresent()){
            return false;
        }

        return true;
    }

    /**
     * 회원가입
     */
    @Transactional
    @Override
    public User join(UserSignupDTO userSignupDTO) {


        Optional<User> result = userRepository.findByNickname(userSignupDTO.getNickname());

        if(result.isPresent()){
            throw new CustomApiException("이미 등록된 ID 입니다.");
        }


        String rawPassword = userSignupDTO.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);
        userSignupDTO.setPassword(encPassword);

        User user = userSignupDTO.toEntity();

        userRepository.save(user);

        return user;

    }

    /**
     * 프로필사진 업데이트
     */
    @Transactional
    @Override
    public void modifyUserImg(Long userId, MultipartFile multipartFile) throws IOException {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomApiException("존재하는 않은 회원입니다."));

        //서버 컴퓨터에 저장된 기존 프로필 사진 삭제
        fileRemove(user);

        ResultFileStore resultFileStore = fileStore.storeFile(multipartFile);

        //사진 저장
        user.changeUserImg(resultFileStore);
    }


    /**
     *  아이디 변경
     */
    @Transactional
    @Override
    public void modifyUserId(Long userId, String updateNickname) {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomApiException("존재하지 않은 회원입니다."));

        user.changeUserId(updateNickname);
    }


    /**
     * 비밀번호 변경
     */
    @Transactional
    @Override
    public void modifyPassword(Long userId, String password) {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomApiException("존재하지 않은 회원입니다."));

        //암호화
        String encPassword = passwordEncoder.encode(password);

        user.changePassword(encPassword);
    }


    /**
     * 기존 비밀번호 검증
     */
    @Transactional(readOnly = true)
    @Override
    public Boolean verifyPassword(Long userId, String password) {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomApiException("존재하지 않은 회원입니다."));

        System.out.println(password);

        if(passwordEncoder.matches(password, user.getPassword())){
            return true;
        }

        return false;
    }


    /**
     * 프로필 조회
     */
    @Transactional(readOnly = true)
    @Override
    public UserResDTO getUser(Long userId) throws IOException {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomApiException("존재하지 않은 회원입니다."));

        UserResDTO userResDTO = user.toDTO();

        //이미지 파일 데이터
        if(!(user.getStoreFilename().isBlank())){
            File file = new File(fileStore.getFullPath(user.getFolderPath(), user.getStoreFilename()));
            byte[] bytes = FileCopyUtils.copyToByteArray(file);
            userResDTO.setImageData(bytes);
        }


        return userResDTO;
    }


    /**
     * 북마크 DTO 리스트
     */
    @Transactional(readOnly = true)
    @Override
    public BookmarkDTO getListOfBookmark(Long userId) {

        //bookmark = true 인 phrase, link 리스트
        List<Phrase> phraseList = phraseRepository.getPhraseBookmarkList(userId);
        List<Link> linkList = linkRepository.getLinkBookmarkList(userId);

        //bookmark = true 인 phraseDTO 리스트
        List<PhraseResDTO> phraseResDTOList = phraseList.stream().map(phrase -> {
            PhraseResDTO phraseResDTO = phrase.toResDTO();
            return phraseResDTO;
        }).collect(Collectors.toList());


        //bookmark = true 인 linkDTO 리스트
        List<LinkResDTO> linkResDTOList = linkList.stream().map(link -> {
            LinkResDTO linkResDTO = link.toResDTO();
            return linkResDTO;
        }).collect(Collectors.toList());


        return BookmarkDTO.builder()
                .phraseResDTOList(phraseResDTOList)
                .linkResDTOList(linkResDTOList)
                .build();
    }

    /**
     * 아이디 찾기
     */
    @Transactional(readOnly = true)
    @Override
    public String findNickname(String phone) {

        String nickname = userRepository.nicknameFindByPhone(phone).orElseThrow(() ->
                new CustomApiException("존재하지 않은 회원입니다."));

        return nickname;
    }

    /**
     * 아이디 체크 (비밀번호 찾기)
     */
    @Transactional(readOnly = true)
    @Override
    public Long checkNickname(String nickname) {

        User user = userRepository.findByNickname(nickname).orElseGet(() ->
                User.builder().userId(-1L).build());

        return user.getUserId();
    }

    /**
     * 핸드폰 번호 검증 (비밀번호 찾기)
     */
    @Transactional(readOnly = true)
    @Override
    public Boolean verifyPhone(Long userId, String phone) {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomApiException("존재하지 않은 회원입니다."));

        if(user.getPhone().equals(phone)){
            return true;
        }
        else{
            return false;
        }
    }


    /**
     * 회원 탈퇴
     */
    @Transactional
    @Override
    public void removeUser(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomApiException("존재하지 않은 회원입니다."));

        //scheduleSelect -> schedule -> timeoutSelect -> 서버컴퓨터 timeout 사진 -> timeout ->
        // phrase -> link -> 서버컴퓨터 folder 사진 -> folder ->
        // userRoleSet -> 서버컴퓨터 user 사진 -> user 순으로 삭제


        //scheduleSelect, schedule 삭제
        List<Schedule> scheduleList = scheduleRepository.getScheduleList(userId);
        scheduleList.stream().forEach(schedule -> scheduleService.removeSchedule(schedule.getScheduleId()));

        //timeoutSelect, 서버 컴퓨터 timeout 사진, timeout 삭제
        List<Timeout> timeoutList = timeoutRepository.getTimeoutList(userId);
        timeoutList.stream().forEach(timeout -> timeoutService.removeTimeout(timeout.getTimeoutId()));

        //phrase, link, 서버 컴퓨터 folder 사진, folder 삭제
        List<Folder> folderList = folderRepository.topFolderList(userId); //최상위폴더 삭제 (재귀)
        folderList.stream().forEach(folder -> folderService.removeFolder(folder.getFolderId()));


        //서버 컴퓨터 user 사진 삭제
        fileRemove(user);

        //user 삭제
        userRepository.deleteById(userId);
    }

    private void fileRemove(User user) {

        //기존 프로필 이미지가 있을 경우
        if(user.getStoreFilename() != null && !(user.getStoreFilename().equals(""))){
            String folderPath = user.getFolderPath();
            String storeFilename = user.getStoreFilename();

            //원본 이미지 삭제
            File file = new File(fileStore.getFullPath(folderPath, storeFilename));
            file.delete();

            //썸네일 이미지 삭제
            File thumbnail = new File(fileStore.getThumbnailFullPath(folderPath, storeFilename));
            thumbnail.delete();
        }
    }


}
