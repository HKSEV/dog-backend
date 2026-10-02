package com.ksh.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "adoption_campaign")
@Getter
@Setter
@NoArgsConstructor
public class AdoptionCampaign {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "campaign_id")
  private Long id;

  @Column(nullable = false, length = 50)
  private String hashtag;

  @Column(nullable = false, length = 256)
  private String title;

  @Lob
  // 이미지, 동영상, 긴 텍스트 같은 대용량 데이터를
  // 저장하기 위한 가변 길이 데이터 타입
  @Column(name = "cont_bdy")
  private String content;

  @Column(name = "thumbnail_url", length = 1000)
  private String thumbnailUrl;

  @Column(name = "media_type", length = 20)
  private String mediaType;

  @Column(name = "media_url", length = 1000)
  private String mediaUrl;

  @CreationTimestamp
  @Column(name = "insert_dt", updatable = false)
  private LocalDateTime insertDt;
}
