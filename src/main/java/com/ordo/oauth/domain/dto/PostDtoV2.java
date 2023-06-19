package com.ordo.oauth.domain.dto;

import com.ordo.oauth.domain.entity.CommentEntity;
import com.ordo.oauth.domain.entity.CommentEntityV2;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostDtoV2 {

  private Integer id;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private String title;

  private String body;

  private Optional<List<CommentDto>> comment;

}
