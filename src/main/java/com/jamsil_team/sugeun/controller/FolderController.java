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
    @GetMapping("{type}")
    public ResponseEntity<List<FolderResDTO>> typeFolderList(@PathVariable("user-id") String userId,
                                                          @PathVariable("type") FolderType type){

        List<FolderResDTO> folderResDTOList = folderService.getListOfFolder(userId, type, null);

        return new ResponseEntity<>(folderResDTOList, HttpStatus.OK);
    }

    /**
     *  폴더 생성
     */
    @PostMapping
    public ResponseEntity<String> createFolder(@PathVariable("user-id") String userId,
                                               @RequestBody FolderDTO folderDTO) throws IOException {

        folderService.createFolder(folderDTO);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


    /**
     *  폴더 조회
     */
    @GetMapping("{folder-id}")
    public ResponseEntity<DetailFolderDTO> readFolder(@PathVariable("user-id") String userId,
                                       @PathVariable("folder-id") Long folderId){

        DetailFolderDTO detailFolderDTO = folderService.getFolder(userId, folderId);

        return new ResponseEntity(detailFolderDTO, HttpStatus.OK);
    }

}
