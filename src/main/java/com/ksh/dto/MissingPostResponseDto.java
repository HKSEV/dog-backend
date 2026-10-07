package com.ksh.dto;

import com.ksh.domain.PostStatus;
import com.ksh.entity.MissingPost;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MissingPostResponseDto {
  private Long id;
  private String title, content, breed, gender, rescueLocation, authorName;
  private PostStatus status;
  private List<String> mediaUrls;
  private LocalDateTime createdAt;

  public MissingPostResponseDto(MissingPost post) {
    this.id = post.getId();
    this.title = post.getTitle();
    this.status = post.getStatus();
    this.content = post.getContent();
    this.breed = post.getBreed();
    this.gender = post.getGender();
    this.rescueLocation = post.getRescueLocation();
    this.authorName = post.getAuthor() != null
            ? post.getAuthor().getUsername()
            : "알 수 없음";
    this.mediaUrls = post.getMediaUrls();
  }
}
