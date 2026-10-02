package com.ksh.dto;

import lombok.*;

@Data
@AllArgsConstructor // 생성자 자동 생성
public class AdminResponse {
  private String name; // 관리자 이름
  private String message; // 로그인 성공 alert 메시지
}
