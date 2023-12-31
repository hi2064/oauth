package com.ordo.oauth.domain.response;

import com.ordo.oauth.domain.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CommentResponse {
    private Integer id;
    private String comment;
    private String userName;
    private Integer postId;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public static CommentResponse fromComment(CommentEntity comment){
        return new CommentResponse(
                comment.getId(),
                comment.getComment(),
                comment.getUser().getUserName(),
                comment.getPost().getId(),
                comment.getCreatedAt(),
                comment.getLastModifiedAt()
        );
    }
}
