package com.ksh.entity;

import com.ksh.domain.PostStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "missing_posts")
@Getter
@Setter
public class MissingPost {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PostStatus status = PostStatus.MISSING;

  private String breed;
  private String gender;
  private String age;
  private String weight;
  private String color;
  private String rescueLocation;

  @ElementCollection
  private List<String> mediaUrls;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private Member author;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
