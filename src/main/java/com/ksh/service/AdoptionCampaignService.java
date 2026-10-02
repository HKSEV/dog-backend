package com.ksh.service;

import com.ksh.dto.AdoptionCampaignRequest;
import com.ksh.entity.AdoptionCampaign;
import com.ksh.repository.AdoptionCampaignRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 이 클래스가 핵심 비즈니스 로직이 흐르는 곳이라고 알림
@Transactional(readOnly = true)
public class AdoptionCampaignService {
  // 의존성 주입(Dependency Injection)
  private final AdoptionCampaignRepository adoptionCampaignRepository;

  public AdoptionCampaignService(AdoptionCampaignRepository adoptionCampaignRepository) {
    this.adoptionCampaignRepository = adoptionCampaignRepository;
    // 전달받은 Repository 객체를 클래스 전역에서 쓸 수 있도록 변수에 저장
  }
  // 전체 캠페인 목록 조회
  public List<AdoptionCampaign> getAllCampaigns() {
    return adoptionCampaignRepository.findAll();
    // 모든 캠페인 데이터를 모두 조회해서 가져옴
  }
  // 해시태그별 캠페인 목록 조회 메소드
  public List<AdoptionCampaign> getCampaignsByHashtag(String hashtag) {
    return adoptionCampaignRepository.findByHashtag(hashtag);
    // Repository에 미리 만들어 둔 규칙을 실행해 데이터 추출
  }
  @Transactional
  // 위에서 클래스 단위로 읽기 전용을 걸어두었기 때문에 데이터를 추가,수정,삭제하기 위해 읽기전용을 해제하고 쓰기 권한을 열어주기 위해 붙임
  public void registerCampaign(AdoptionCampaignRequest request) {
    AdoptionCampaign campaign = new AdoptionCampaign();
    campaign.setHashtag(request.getHashtag());
    campaign.setTitle(request.getTitle());
    campaign.setContent(request.getContent());
    campaign.setThumbnailUrl(request.getThumbnailUrl());
    campaign.setMediaType(request.getMediaType());
    campaign.setMediaUrl(request.getMediaUrl());

    adoptionCampaignRepository.save(campaign);
  }
}
