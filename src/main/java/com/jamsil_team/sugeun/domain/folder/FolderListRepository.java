package com.jamsil_team.sugeun.domain.folder;

import java.util.List;

public interface FolderListRepository {

    List<Folder> getListFolder(String userId, FolderType type, Long parentFolderId);

}
