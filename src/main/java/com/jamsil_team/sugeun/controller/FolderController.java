package com.jamsil_team.sugeun.controller;

import com.jamsil_team.sugeun.domain.folder.Folder;
import com.jamsil_team.sugeun.domain.folder.FolderType;
import com.jamsil_team.sugeun.dto.FolderDTO;
import com.jamsil_team.sugeun.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
