package com.ksh.controller;

import com.ksh.dto.MissingPostRequestDto;
import com.ksh.dto.MissingPostResponseDto;
import com.ksh.service.MissingPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/missing-posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class MissingPostController {
  private final MissingPostService service;

  // 실종 신고 글 작성 API(POST)
  @PostMapping
  public ResponseEntity<MissingPostResponseDto> createPost(
          @RequestPart("dto") MissingPostRequestDto requestDto,
          @RequestPart(value = "file", required = false) MultipartFile file,
          // 인증 정보를 가져오는 Principal
          Principal principal) {
    try {
      // 파일이 첨부된 경우 서버 지정 경로에 저장 후 URL 추출
      if (file != null && !file.isEmpty()) {
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        java.nio.file.Path path = java.nio.file.Paths.get(uploadDir + filename);

        java.nio.file.Files.createDirectories(path.getParent());
        java.nio.file.Files.write(path, file.getBytes());

        String imageUrl = "http://localhost:8080/uploads/" + filename;

        if (requestDto.getMediaUrls() == null) {
          requestDto.setMediaUrls(new java.util.ArrayList<>());
        }
        requestDto.getMediaUrls().add(imageUrl);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    // 서비스 호출 및 응답 반환
    MissingPostResponseDto response = service.createPost(requestDto, file, principal.getName());
    return ResponseEntity.ok(response);
  }

  // 무한 스크롤 조회 API(GET)
  @GetMapping
  public ResponseEntity<List<MissingPostResponseDto>> getPosts(
          @RequestParam(required = false) Long cursorId,
          @RequestParam(defaultValue = "10") int size) {
    List<MissingPostResponseDto> posts = service.getPostsByScroll(cursorId, size);
    return ResponseEntity.ok(posts);
  }

  // 글 수정 API(PUT)
  @PutMapping("/{id}")
  public ResponseEntity<MissingPostResponseDto> updatePost(
          @PathVariable Long id,
          @RequestBody MissingPostRequestDto requestDto,
          Principal principal) {
    MissingPostResponseDto response = service.updatePost(id, requestDto, principal.getName());
    return ResponseEntity.ok(response);
  }

  // 글 삭제 API(DELETE)
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePost(@PathVariable Long id, Principal principal) {
    service.deletePost(id, principal.getName());
    return ResponseEntity.ok().build();
  }

  // 완료 처리 API(PATCH)
  @PatchMapping("/{id}/complete")
  public ResponseEntity<Void> completePost(@PathVariable Long id, Principal principal) {
    service.completePost(id, principal.getName());
    return ResponseEntity.ok().build();
  }
}
