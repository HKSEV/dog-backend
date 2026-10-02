package com.ksh.controller;

import com.ksh.dto.AdoptionCampaignRequest;
import com.ksh.dto.AdoptionCampaignResponse;
import com.ksh.entity.AdoptionCampaign;
import com.ksh.service.AdoptionCampaignService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class AdoptionCampaignController {
  private final AdoptionCampaignService adoptionCampaignService;

  @GetMapping
  public List<AdoptionCampaign> getCampaigns(@RequestParam(required = false) String hashtag) {
    if (hashtag != null && !hashtag.trim().isEmpty())
      return adoptionCampaignService.getCampaignsByHashtag(hashtag);

    return adoptionCampaignService.getAllCampaigns();
  }
  // 2.캠페인 등록(관리자 권한 세션 체크 필수)
  @PostMapping
  public ResponseEntity<?> registerCampaign(
          @RequestBody AdoptionCampaignRequest request, HttpServletRequest httpRequest) {
    // 세션 검사
    HttpSession session = httpRequest.getSession(false);
    if (session == null || session.getAttribute("adminName") == null)
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
              new AdoptionCampaignResponse("관리자 로그인이 필요한 서비스입니다.")
      );

    try {
      adoptionCampaignService.registerCampaign(request);
      return ResponseEntity.ok(new AdoptionCampaignResponse("성공적으로 등록되었습니다."));
    }
    catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
              new AdoptionCampaignResponse("등록 중 서버 오류 발생: " + e.getMessage())
      );
    }
  }
}
