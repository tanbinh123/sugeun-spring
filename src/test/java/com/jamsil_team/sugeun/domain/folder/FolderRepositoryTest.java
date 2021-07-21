package com.jamsil_team.sugeun.domain.folder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class FolderRepositoryTest {

    @Autowired FolderRepository folderRepository;

    @Test
    void 폴더_리스트() throws Exception{
        //given

        //when
        List<Folder> result = folderRepository.getListFolder("hyeongwoo");
        for (Folder folder : result){
            System.out.println(folder);
        }
        //then
    }

}