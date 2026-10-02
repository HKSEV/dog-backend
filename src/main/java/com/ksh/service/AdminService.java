package com.ksh.service;

import com.ksh.dto.AdminResponse;
import com.ksh.entity.Admin;
import com.ksh.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service // 이 클래스가 핵심 비즈니스 로직이 흐르는 곳이라고 알림
@RequiredArgsConstructor
public class AdminService {
  private final AdminRepository adminRepository;

  public AdminResponse authenticate(String email, String password) {
    Admin admin = adminRepository.findByMemberEmail(email).orElseThrow(() ->
            new IllegalArgumentException("관리자 권한이 없거나 존재하지 않습니다."));
    if (!admin.getMember().getPassword().equals(password))
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");

    return new AdminResponse(admin.getMember().getName(), "관리자 로그인 성공!");
  }
}
