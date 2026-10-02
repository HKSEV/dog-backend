package com.ksh.service;

import com.ksh.entity.YoutubePost;
import com.ksh.repository.YoutubePostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 이 클래스가 핵심 비즈니스 로직이 흐르는 곳이라고 알림
@Transactional(readOnly = true) // 읽기 전용
public class YoutubePostService {
  // Repository 값이 바뀌지 않도록 불변성 보장
  private final YoutubePostRepository youtubePostRepository;

  public YoutubePostService(YoutubePostRepository youtubePostRepository) {
    this.youtubePostRepository = youtubePostRepository;
  }
  // 1.유튜브 목록 조회(최신순)
  public List<YoutubePost> getAllYoutubePosts() {
    return youtubePostRepository.findAllByOrderByInsertDtDesc();
  }
  // 2.유튜브 등록
  @Transactional // 수정하기 위한 어노테이션
  public YoutubePost registerYoutubePost(YoutubePost youtubePost) {
    return youtubePostRepository.save(youtubePost);
  }
}
