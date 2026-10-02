package com.ksh.service;

import com.ksh.entity.NeedHelp;
import com.ksh.repository.NeedHelpRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 이 클래스가 핵심 비즈니스 로직이 흐르는 곳이라고 알림
@Transactional(readOnly = true) // 읽기 전용
public class NeedHelpService {
  private final NeedHelpRepository needHelpRepository;

  public NeedHelpService(NeedHelpRepository needHelpRepository) {
    this.needHelpRepository = needHelpRepository;
  }
  // 1.도움 목록 조회(최신순)
  public List<NeedHelp> getAllNeedHelps() {
    return needHelpRepository.findAllByOrderByInsertDtDesc();
  }
  // 2.도움 등록
  @Transactional
  public NeedHelp registerNeedHelp(NeedHelp needHelp) {
    return needHelpRepository.save(needHelp);
  }
}
