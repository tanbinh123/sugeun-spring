package com.jamsil_team.sugeun.file;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Log4j2
@Component
public class FileStore {


    private String uploadFolder = System.getProperty("user.dir");

    public String getFullPath(String folderPath, String storeFilename){

        return uploadFolder + File.separator + folderPath + File.separator + storeFilename;

    }

    public String getThumbnailFullPath(String folderPath, String storeThumbnailFilename){

        return uploadFolder + File.separator + folderPath + File.separator + storeThumbnailFilename;
    }

    public ResultFileStore storeFile(MultipartFile multipartFile) throws IOException {

        if(multipartFile == null){
            return null;
        }

        //확장자 체크
        if(multipartFile.getContentType().startsWith("image") == false){
            log.warn("this file is not image type");
            throw new IllegalStateException("이미지 파일이 아닙니다.");
        }

        //파일이름
        String originalFilename = multipartFile.getOriginalFilename();
        log.info("originFilename: " + originalFilename);

        //파일 저장 이름
        String storeFilename = createStoreFilename(originalFilename);
        log.info("storeFilename: " + storeFilename);

        //폴더 생성
        String folderPath = makeFolder();

        //서버 컴퓨터에 이미지 저장 (upload/sugeun/연/월/일/uuid + "_" + originalFilename)
        File imageFile = new File(getFullPath(folderPath, storeFilename));

        System.out.println("================");
        System.out.println(imageFile.getAbsolutePath());

        multipartFile.transferTo(imageFile);

        //서버 컴퓨터에 썸네일 이미지 저장 (upload/sugeun/연/월/일/"s_"+ uuid + "_" + originalFilename)
        String storeThumbnailFile = createStoreThumbnailFile(storeFilename);

        File thumbnailFile = new File(getThumbnailFullPath(folderPath, storeThumbnailFile));

        //TODO 실제 이미지 파일로 넣어야 생성됨
        //Thumbnailator.createThumbnail(imageFile, thumbnailFile, 100, 100);

        return new ResultFileStore(folderPath, storeFilename);
    }

    private String makeFolder() {

        String folderPath = "upload" + File.separator;

        String str = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        folderPath += str.replace("//", File.separator);

        log.info("folderPath: " + folderPath);

        File uploadPathFolder = new File(uploadFolder, folderPath);

        if(uploadPathFolder.exists() == false){
            uploadPathFolder.mkdirs();
        }

        return folderPath;
    }

    private String createStoreFilename(String originalFilename) {

        String uuid = UUID.randomUUID().toString(); //파일명 고유성 유지

        return uuid + "_" + originalFilename;
    }

    private String createStoreThumbnailFile(String storeFilename) {

        return "s_" + storeFilename;
    }




}
