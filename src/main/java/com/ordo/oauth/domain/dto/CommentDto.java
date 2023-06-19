package com.ordo.oauth.domain.dto;

import com.ordo.oauth.domain.entity.BaseEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentDto extends BaseEntity {

  private Integer id;

//  private LocalDateTime createdAt;
//
//  private LocalDateTime updatedAt;

  private String comment;

  private Integer postId;

  private Integer userId;

  private String userName;

}
