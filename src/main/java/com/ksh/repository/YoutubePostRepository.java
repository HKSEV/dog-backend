package com.ksh.repository;

import com.ksh.entity.YoutubePost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface YoutubePostRepository extends JpaRepository<YoutubePost, Long> {
  List<YoutubePost> findAllByOrderByInsertDtDesc();
}
