package com.ordo.oauth.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor
@DynamicUpdate
@Table(name = "comment", catalog = "bdlee")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class CommentEntityV2 extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id = null;

  @Column(name = "comment")
  private String comment;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Column(name = "post_id")
  private Integer postId;

  @Column(name = "user_id")
  private Integer userId;

  @Builder
  public CommentEntityV2(String comment, Integer postId, Integer userId){
    this.comment = comment;
    this.postId = postId;
    this.userId = userId;
  }

  public void update(String comment){
    this.comment = comment;
  }



}
