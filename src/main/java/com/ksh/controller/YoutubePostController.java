package com.ksh.controller;

import com.ksh.entity.YoutubePost;
import com.ksh.service.YoutubePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 출처 허용 옵션
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController // 프론트로는 사용하지 않고 db로만 json
@RequestMapping("/api/youtube")
@RequiredArgsConstructor //생성자를 만듦
public class YoutubePostController {
  // 비즈니스 로직을 사용하기 위해서
  private final YoutubePostService youtubePostService;

  // 유튜브 목록조회 API
  @GetMapping
  public List<YoutubePost> getYoutubePosts() {
    return youtubePostService.getAllYoutubePosts();
  }

  @PostMapping
  public ResponseEntity<?> registerYoutubePost(@RequestBody YoutubePost youtubePost) {
    try {
      YoutubePost savedPost = youtubePostService.registerYoutubePost(youtubePost);
      return ResponseEntity.ok("유튜브 영상이 성공적으로 등록되었습니다!");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        "유튜브 영상 등록 중 서버 오류가 발생했습니다."
      );
    }
  }
}
