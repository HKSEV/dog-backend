package com.ksh.repository;

import com.ksh.entity.MissingPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissingPostRepository extends JpaRepository<MissingPost, Long> {
  // 무한 스크롤 -> 커서 기반 조회
  @Query("SELECT p FROM MissingPost p WHERE (:cursorId is NULL OR p.id < :cursorId) ORDER BY p.id DESC")
  List<MissingPost> findAllByCursor(@Param("cursorId") Long cursorId, Pageable pageable);
}
