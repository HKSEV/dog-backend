package com.ksh.service;

import com.ksh.entity.FellowNews;
import com.ksh.repository.FellowNewsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 이 클래스가 핵심 비즈니스 로직이 흐르는 곳이라고 알림
@Transactional(readOnly = true) // 읽기 전용
public class FellowNewsService {
  private final FellowNewsRepository fellowNewsRepository;

  public FellowNewsService(FellowNewsRepository fellowNewsRepository) {
    this.fellowNewsRepository = fellowNewsRepository;
  }
  // 1.뉴스 목록 조회(최신순)
  public List<FellowNews> getAllFellowNews() {
    return fellowNewsRepository.findAllByOrderByInsertDtDesc();
  }
  // 2.뉴스 등록
  @Transactional
  public FellowNews registerFellowNews(FellowNews fellowNews) {
    return fellowNewsRepository.save(fellowNews);
  }
}
