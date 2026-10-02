package com.ksh.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recommended_animals")
@Data
@NoArgsConstructor
public class RecommendedAnimal {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // DIRECT, FACEBOOK, INSTAGRAM 중 하나 지정
  @Column(nullable = false, length = 20)
  private String sourceType;

  // 외부 링크 주소(직접 등록 경우에 비어있을 수 있으므로 nullable=true)
  @Column(length = 500)
  private String sourceUrl;

  @Column(nullable = false, length = 20)
  private String region;

  @Column(nullable = false, length = 100)
  private String noticeNo;

  @Column(nullable = false, length = 10)
  private String birthYear;

  @Column(nullable = false, length = 5)
  private String gender;

  @Column(nullable = false, length = 20)
  private Double weight;

  @Column(nullable = false, length = 1000)
  private String imageUrl;
}
