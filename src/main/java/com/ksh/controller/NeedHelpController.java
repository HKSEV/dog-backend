package com.ksh.controller;

import com.ksh.entity.NeedHelp;
import com.ksh.service.NeedHelpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/need-help")
@RequiredArgsConstructor
public class NeedHelpController {
  private final NeedHelpService needHelpService;

  // 유튜브 목록조회 API
  @GetMapping
  public List<NeedHelp> getNeedHelps() {
    return needHelpService.getAllNeedHelps();
  }

  @PostMapping
  public ResponseEntity<?> registerNeedHelp(@RequestBody NeedHelp needHelp) {
    try {
      NeedHelp savedHelp = needHelpService.registerNeedHelp(needHelp);
      return ResponseEntity.ok("도움요청글이 성공적으로 등록되었습니다!");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        "도움요청글 등록 중 서버 오류가 발생했습니다."
      );
    }
  }
}
