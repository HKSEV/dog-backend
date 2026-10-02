package com.ksh.dto;

import com.ksh.domain.Gender;
import com.ksh.domain.ShelterStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class ShelterAnimalRequestDto {
  private ShelterStatus status;
  private Gender gender;
  private String breed;
  private String noticeNo;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate regDate;

  private String rescueLocation;
  private String content;

  private String imageUrl;
  private MultipartFile imageFile;
}
