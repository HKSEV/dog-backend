package com.ksh.controller;

import com.ksh.dto.AdminRequest;
import com.ksh.dto.AdminResponse;
import com.ksh.entity.Admin;
import com.ksh.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
  private final AdminService adminService;

  @PostMapping("/login") // 상태 코드와 데이터
  public ResponseEntity<?> login(@RequestBody AdminRequest request, HttpServletRequest httpRequest) {
    try {
      AdminResponse response = adminService.authenticate(
        request.getEmail(),
        request.getPassword()
      );

      HttpSession session = httpRequest.getSession();
      session.setAttribute("adminName", response.getName());
      session.setMaxInactiveInterval(3600);

      return ResponseEntity.ok(response);
    }
    catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                      Collections.singletonMap("message", e.getMessage()));
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest httpRequest) {
    // getSession(false): 세션이 존재하면 반환, 없으면 null 반환 (새로 생성하지 않음)
    HttpSession session = httpRequest.getSession(false);
    if (session != null)
      session.invalidate(); // 세션 무효화(삭제)

    return ResponseEntity.ok(Collections.singletonMap("message", "로그아웃 되었습니다."));
  }

  @GetMapping("/check")
  public ResponseEntity<?> checkSession(HttpServletRequest httpRequest) {
    // getSession(false): 기존 세션이 없으면 null 반환 (새로 만들지 않음)
    HttpSession session = httpRequest.getSession(false);

    // 세션이 없거나, 세션에 로그인 정보가 없다면 401 에러 반환
    if (session == null || session.getAttribute("adminName") == null)
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
              Collections.singletonMap("message", "세션이 만료되었습니다. 다시 로그인해주세요.")
      );

    // 세션이 유효하다면 사용자 정보 반환 (프론트엔드 상태 복구용)
    String adminName = (String) session.getAttribute("adminName");
    return ResponseEntity.ok(Collections.singletonMap("name", adminName));
  }
}
