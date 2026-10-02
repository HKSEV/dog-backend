package com.ksh.repository;

import com.ksh.domain.ShelterStatus;
import com.ksh.entity.ShelterAnimal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShelterAnimalRepository extends JpaRepository<ShelterAnimal, Long> {
  List<ShelterAnimal> findByStatus(ShelterStatus status);
}
