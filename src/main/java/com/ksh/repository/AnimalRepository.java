package com.ksh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ksh.entity.Animal;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

}
