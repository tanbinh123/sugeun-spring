package com.jamsil_team.sugeun.service;

import com.jamsil_team.sugeun.domain.user.User;
import com.jamsil_team.sugeun.domain.user.UserRepository;
import com.jamsil_team.sugeun.dto.SignupDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${com.jamsil_team.upload.path")
    private String uploadPath;

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
    @Override
    public User join(SignupDTO signUpDTO) {


        Optional<User> result = userRepository.findById(signUpDTO.getUserId());

        if(result.isPresent()){
            throw new IllegalStateException("이미 등록된 ID 입니다.");
        }

        String rawPassword = signUpDTO.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);
        signUpDTO.setPassword(encPassword);

        User user = signUpDTO.toEntity();

        //회원 프로필 사진 저장
        if(signUpDTO.getFile() != null){

            String originalName = signUpDTO.getFile().getOriginalFilename();

            String fileName = originalName.substring(originalName.lastIndexOf("//") + 1);
            log.info("fileName: " + fileName);

            String folderPath = makeFolder();

            String uuid = UUID.randomUUID().toString(); //이미지 네임 고유성 보장

            String saveName = uploadPath + File.separator + folderPath + File.separator +
                    uuid + "_" + fileName;


            Path savePath = Paths.get(saveName);

            try{
                signUpDTO.getFile().transferTo(savePath);

                //섬네일
                String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator +
                        "s_" + uuid + "_" + fileName;

                File thumbnailFile = new File(thumbnailSaveName);

                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);

                user.saveUserImg(folderPath, uuid, fileName);

            } catch (IOException e){
                e.printStackTrace();
            }
        }

        userRepository.save(user);

        return user;

    }

    //폴더 생성
    private String makeFolder() {

        String str = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        log.info("str: " + str);

        String folderPath = str.replace("//", File.separator);

        File uploadPathFolder = new File(uploadPath, folderPath);

        if(uploadPathFolder.exists() == false){
            uploadPathFolder.mkdirs();
        }

        return folderPath;
    }

    /**
     * 로그인 시 deviceToken 갱신
     */
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
     * 기본 비밀번호 검증
     */
    @Override
    public Boolean verifyPassword(String userId, String password) {

        User user = userRepository.findByUserId(userId).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 회원입니다."));

        return passwordEncoder.matches(password, user.getPassword());

    }

    /**
     * 비밀번호 변경
     */
    @Override
    public void modifyPassword(String userId, String password) {

        User user = userRepository.findByUserId(userId).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 회원입니다."));

        //암호화
        String encPassword = passwordEncoder.encode(password);

        user.changePassword(encPassword);
    }


}
