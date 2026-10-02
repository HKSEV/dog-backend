package com.ksh.repository;

import com.ksh.entity.NeedHelp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NeedHelpRepository extends JpaRepository<NeedHelp, Long> {
  List<NeedHelp> findAllByOrderByInsertDtDesc();
}
