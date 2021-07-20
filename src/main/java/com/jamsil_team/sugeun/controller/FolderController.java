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

        System.out.println(folderDTO);
        System.out.println(folderDTO.getType().getClass());
        System.out.println(folderDTO.getType().equals(FolderType.PHRASE.toString()));
        System.out.println(folderDTO.getType().equals(FolderType.PHRASE));
        System.out.println(folderDTO.getType().toString().equals(FolderType.PHRASE.toString()));
        System.out.println(folderDTO.getType());
        System.out.println(folderDTO.getType().toString());
        System.out.println(folderDTO.getType().ordinal());
        System.out.println(folderDTO.getType().name());
        System.out.println(folderDTO.getType().compareTo(FolderType.PHRASE));


        Folder folder = folderService.createFolder(folderDTO);

        FolderDTO folderResDTO = folder.toDTO();


        return new ResponseEntity<>(folderResDTO, HttpStatus.OK);
    }

}
