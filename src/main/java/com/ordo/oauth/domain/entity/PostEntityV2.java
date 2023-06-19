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
@Table(name = "post", catalog = "bdlee")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class PostEntityV2 extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id = null;

  @Column(name = "body")
  private String body;

  @Column(name = "title")
  private String title;

  @Column(name = "user_id")
  private Integer userId;

  @Builder
  public PostEntityV2(String title, String body, Integer userId){
    this.title = title;
    this.body = body;
    this.userId = userId;
  }

  public void update(String title, String body){
    this.title = title;
    this.body = body;
  }
}
