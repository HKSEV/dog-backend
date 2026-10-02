package com.ksh.service;

import com.ksh.dto.AnimalRequest;
import com.ksh.entity.RecommendedAnimal;
import com.ksh.repository.RecommendedAnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service // 이 클래스가 핵심 비즈니스 로직이 흐르는 곳이라고 알림
@RequiredArgsConstructor
public class AnimalService {
  private final RecommendedAnimalRepository animalRepository;

  // 동물을 등록(저장)하는 핵심 기능
  public void registerAnimal(AnimalRequest request) {
    // 1.텅 빈 엔티티(테이블 데이터 한줄)를 하나 새로 만듦
    RecommendedAnimal animal = new RecommendedAnimal();
    // 2.프론트엔드에서 받아온 DTO의 데이터를 엔티티에 하나씩 옮겨 담음
    animal.setSourceType(request.getSourceType());
    animal.setSourceUrl(request.getSourceUrl());
    animal.setRegion(request.getRegion());
    animal.setNoticeNo(request.getNoticeNo());
    animal.setBirthYear(request.getBirthYear());
    animal.setGender(request.getGender());
    animal.setWeight(request.getWeight());
    animal.setImageUrl(request.getImageUrl());
    // Repository에 저장
    animalRepository.save(animal);
  }
}
