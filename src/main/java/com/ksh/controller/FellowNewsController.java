package com.ksh.controller;

import com.ksh.entity.FellowNews;
import com.ksh.service.FellowNewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/fellow-news")
@RequiredArgsConstructor
public class FellowNewsController {
  private final FellowNewsService fellowNewsService;

  // 유튜브 목록조회 API
  @GetMapping
  public List<FellowNews> getFellowNews() {
    return fellowNewsService.getAllFellowNews();
  }

  @PostMapping
  public ResponseEntity<?> registerFellowNews(@RequestBody FellowNews fellowNews) {
    try {
      FellowNews savedNews = fellowNewsService.registerFellowNews(fellowNews);
      return ResponseEntity.ok("펠로우 소식이 성공적으로 등록되었습니다!");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        "펠로우 소식 등록 중 서버 오류가 발생했습니다."
      );
    }
  }
}
