package com.jamsil_team.sugeun.domain.folder;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;

@Log4j2
public class FolderListRepositoryImpl extends QuerydslRepositorySupport implements FolderListRepository{

    public FolderListRepositoryImpl() {
        super(Folder.class);
    }

    @Override
    public List<Folder> getListFolder(String userId, FolderType type, Long parentFolderId) {
        QFolder folder = QFolder.folder;

        //from절
        JPQLQuery<Folder> jpqlQuery = from(folder);

        //select절
        JPQLQuery<Folder> tuple = jpqlQuery.select(folder);

        //where절
        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression expression = folder.folderId.gt(0L);

        builder.and(expression);

        if(type == null){
            builder.and(folder.user.userId.eq(userId));
            if(parentFolderId == null){
                builder.and(folder.parentFolder.folderId.isNull());
            }else{
                builder.and(folder.parentFolder.folderId.eq(parentFolderId));
            }
        }
        else{
            builder.and(folder.user.userId.eq(userId));
            builder.and(folder.type.eq(type));
            if(parentFolderId == null){
                builder.and(folder.parentFolder.folderId.isNull());
            }else{
                builder.and(folder.parentFolder.folderId.eq(parentFolderId));
            }
        }

        tuple.where(builder);

        List<Folder> folderList = tuple.fetch();

        return folderList;
    }
}
