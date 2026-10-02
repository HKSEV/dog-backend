package com.ksh.repository;

import com.ksh.entity.FellowNews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FellowNewsRepository extends JpaRepository<FellowNews, Long> {
  List<FellowNews> findAllByOrderByInsertDtDesc();
}
