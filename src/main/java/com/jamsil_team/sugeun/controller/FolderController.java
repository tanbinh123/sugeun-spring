package com.jamsil_team.sugeun.controller;

import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.domain.folder.FolderType;
import com.jamsil_team.sugeun.dto.DetailFolderDTO;
import com.jamsil_team.sugeun.dto.FolderDTO;
import com.jamsil_team.sugeun.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users/{user-id}/folders")
@RequiredArgsConstructor
@RestController
public class FolderController {

    private final FolderService folderService;

    @PostMapping
    public ResponseEntity<FolderDTO> createFolder(@PathVariable("user-id") String userId,
                                               @RequestBody FolderDTO folderDTO){

        Folder folder = folderService.createFolder(folderDTO);

        FolderDTO folderResDTO = folder.toDTO();

        return new ResponseEntity<>(folderResDTO, HttpStatus.OK);
    }

    @GetMapping("{type}")
    public ResponseEntity<List<FolderDTO>> typeFolderList(@PathVariable("user-id") String userId,
                                                          @PathVariable("type") FolderType type){

        List<FolderDTO> folderDTOList = folderService.getListOfFolder(userId, type, null);

        return new ResponseEntity<>(folderDTOList, HttpStatus.OK);
    }

    @GetMapping("{folder-id}")
    public ResponseEntity<DetailFolderDTO> readFolder(@PathVariable("user-id") String userId,
                                       @PathVariable("folder-id") Long folderId){

        DetailFolderDTO detailFolderDTO = folderService.getFolder(userId, folderId);

        return new ResponseEntity(detailFolderDTO, HttpStatus.OK);
    }

}
