package com.jamsil_team.sugeun.controller;

import com.jamsil_team.sugeun.domain.folder.FolderType;
import com.jamsil_team.sugeun.dto.folder.DetailFolderDTO;
import com.jamsil_team.sugeun.dto.folder.FolderDTO;
import com.jamsil_team.sugeun.dto.folder.FolderResDTO;
import com.jamsil_team.sugeun.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping("/users/{user-id}/folders")
@RequiredArgsConstructor
@RestController
public class FolderController {

    private final FolderService folderService;


    /**
     *  폴더 DTO 리스트
     */
    @GetMapping("/{type}")
    public ResponseEntity<List<FolderResDTO>> typeFolderList(@PathVariable("user-id") Long userId,
                                                          @PathVariable(value = "type", required = false) FolderType type){

        List<FolderResDTO> folderResDTOList = folderService.getListOfFolder(userId, type, null);

        return new ResponseEntity<>(folderResDTOList, HttpStatus.OK);
    }

    /**
     *  폴더 생성
     */
    @PostMapping
    public ResponseEntity<String> createFolder(@PathVariable("user-id") Long userId,
                                               @RequestBody FolderDTO folderDTO) throws IOException {

        folderService.createFolder(folderDTO);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


    /**
     *  폴더 조회
     */
    @GetMapping("{folder-id}")
    public ResponseEntity<DetailFolderDTO> readFolder(@PathVariable("user-id") Long userId,
                                       @PathVariable("folder-id") Long folderId){

        DetailFolderDTO detailFolderDTO = folderService.getFolder(userId, folderId);

        return new ResponseEntity(detailFolderDTO, HttpStatus.OK);
    }

    /**
     * 폴더 이미지 업로드
     */
    @PatchMapping("/{folder-id}/image")
    public ResponseEntity<String> modifyFolderImg(@PathVariable("user-id") Long userId,
                                                  @PathVariable("folder-id") Long folderId,
                                                  @ModelAttribute FolderDTO folderDTO) throws IOException {

        folderService.modifyFolderImage(folderId, folderDTO.getImageFile());

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    /**
     * 폴더 이름 변경
     */
    @PatchMapping("/{folder-id}/name")
    public ResponseEntity<String> modifyFolderName(@PathVariable("user-id") Long userId,
                                                  @PathVariable("folder-id") Long folderId,
                                                  @RequestBody FolderDTO folderDTO){

        folderService.modifyFolderName(folderId, folderDTO.getFolderName());

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    /**
     * 폴더 삭제
     */
    @DeleteMapping("/{folder-id}")
    public ResponseEntity<String> removeFolder(@PathVariable("user-id") Long userId,
                                               @PathVariable("folder-id") Long folderId){

        folderService.removeFolder(folderId);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
