package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.link.Link;
import com.jamsil_team.sugeun.domain.link.LinkRepository;
import com.jamsil_team.sugeun.domain.phrase.Phrase;
import com.jamsil_team.sugeun.domain.phrase.PhraseRepository;
import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.domain.user.UserRepository;

import com.jamsil_team.sugeun.dto.link.LinkDTO;
import com.jamsil_team.sugeun.dto.phrase.PhraseDTO;
import com.jamsil_team.sugeun.dto.user.BookmarkDTO;
import com.jamsil_team.sugeun.dto.user.UserDTO;
import com.jamsil_team.sugeun.dto.user.UserSignupDTO;
import com.jamsil_team.sugeun.file.FileStore;
import com.jamsil_team.sugeun.file.ResultFileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

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

    /**
     * 아이디 중복확인
     */
    @Transactional(readOnly = true)
    @Override
    public Boolean isDuplicateNickname(String userId) {

        Optional<User> result = userRepository.findById(userId);

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


        Optional<User> result = userRepository.findById(userSignupDTO.getUserId());

        if(result.isPresent()){
            throw new IllegalStateException("이미 등록된 ID 입니다.");
        }


        String rawPassword = userSignupDTO.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);
        userSignupDTO.setPassword(encPassword);

        User user = userSignupDTO.toEntity();

        userRepository.save(user);

        return user;

    }


    /**
     * 로그인 시 deviceToken 갱신
     */
    @Transactional
    @Override
    public void UpdateDeviceToken(String userId, String deviceToken) {

        Optional<User> result = userRepository.findById(userId);

        if(result.isPresent()){
            User user = result.get();
            user.changeDeviceToken(deviceToken);
        }
        else{
            throw new IllegalStateException("존재하지 않는 ID 입니다.");
        }
    }

    /**
     * 프로필사진 업데이트
     */
    @Transactional
    @Override
    public void modifyUserImg(String userId, MultipartFile multipartFile) throws IOException {

        User user = userRepository.findByUserId(userId).orElseThrow(() ->
                new IllegalStateException("존재하는 않은 회원입니다."));

        //서버 컴퓨터에 저장된 기존 프로필 사진 삭제
        removeImageFile(user);

        ResultFileStore resultFileStore = fileStore.storeFile(multipartFile);

        //사진 저장
        user.changeUserImg(resultFileStore);
    }

    private void removeImageFile(User user) {

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

    /**
     *  아이디 변경
     */
    /*
    @Transactional
    @Override
    public void modifyUserId(String userId, String updateUserId) {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 회원입니다."));

        user.changeUserId(updateUserId);
    }*/


    /**
     * 비밀번호 변경
     */
    @Transactional
    @Override
    public void modifyPassword(String userId, String password) {

        User user = userRepository.findByUserId(userId).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 회원입니다."));

        //암호화
        String encPassword = passwordEncoder.encode(password);

        user.changePassword(encPassword);
    }


    /**
     * 프로필 조회
     */
    @Transactional(readOnly = true)
    @Override
    public UserDTO getUser(String userId) throws IOException {

        User user = userRepository.findByUserId(userId).orElseThrow(() ->
                new IllegalStateException("존재하지 않은 회원입니다."));

        UserDTO userDTO = user.toDTO();

        //이미지 파일 데이터
        if(!(user.getStoreFilename().isBlank())){
            File file = new File(fileStore.getFullPath(user.getFolderPath(), user.getStoreFilename()));
            byte[] bytes = FileCopyUtils.copyToByteArray(file);
            userDTO.setImageData(bytes);
        }


        return userDTO;
    }

    /**
     * 알람허용 변경
     */
    @Transactional
    @Override
    public void modifyAlarm(String userId) {

        User user = userRepository.findByUserId(userId).orElseThrow(() ->
                new IllegalStateException("존재하지 않은 회원입니다."));

        user.changeAlarm();
    }

    /**
     * 북마크 DTO 리스트
     */
    @Transactional(readOnly = true)
    @Override
    public BookmarkDTO getListOfBookmark(String userId) {

        //bookmark = true 인 phrase, link 리스트
        List<Phrase> phraseList = phraseRepository.getPhraseBookmarkList(userId);
        List<Link> linkList = linkRepository.getLinkBookmarkList(userId);

        //bookmark = true 인 phraseDTO 리스트
        List<PhraseDTO> phraseDTOList = phraseList.stream().map(phrase -> {
            PhraseDTO phraseDTO = phrase.toDTO();
            return phraseDTO;
        }).collect(Collectors.toList());


        //bookmark = true 인 linkDTO 리스트
        List<LinkDTO> linkDTOList = linkList.stream().map(link -> {
            LinkDTO linkDTO = link.toDTO();
            return linkDTO;
        }).collect(Collectors.toList());


        return BookmarkDTO.builder()
                .phraseDTOList(phraseDTOList)
                .linkDTOList(linkDTOList)
                .build();
    }


}
