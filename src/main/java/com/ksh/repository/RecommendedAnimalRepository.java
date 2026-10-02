package com.ksh.repository;

import com.ksh.entity.RecommendedAnimal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendedAnimalRepository extends JpaRepository<RecommendedAnimal, Long> {
  // JpaRepository를 상속받으면 save(저장), findAll(조회) 기능 생김
}
