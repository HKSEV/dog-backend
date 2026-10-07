package com.ksh.security;

// 서블릿 요청/응답 처리를 위한 Jakarta Servlet API 임포트
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Lombok을 사용하여 final 필드에 대한 생성자를 자동으로 생성합니다.
import lombok.RequiredArgsConstructor;

// Spring Security 인증 토큰 객체 및 시큐리티 컨텍스트 관리 클래스 임포트
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

// 문자열 검증 유틸리티 및 웹 요청 필터 기본 클래스 임포트
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtTokenProvider tokenProvider;

  @Override
  protected void doFilterInternal(
          HttpServletRequest request,
          HttpServletResponse response,
          FilterChain filterChain
  ) throws ServletException, IOException {
    try {
      /*
      JSON Web Token :  상태가 없는 인증 토큰
      HTTP 요청 헤더에서 "Authorization" 값을 가져옵니다.
      (클라이언트가 보낸 토큰을 확인하기 위함)

      Cross Site Request Forgery Token

      Bearer : 이증표를 가지고 있는 사람을 주인으로 인증하겠다..
      Bearer 인증은 OAuth 2.0 프레임워크에서 사용하는 토큰 인증 방식
      */
      String bearerToken = request.getHeader("Authorization");
      if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
        String token = bearerToken.substring(7); // 유효성 검증
        if (tokenProvider.validateToken(token)) {
          // 유효한 토큰이라면, 토큰 내부에서 회원의 이름(Username 또는 Subject)을 꺼내옴
          String username = tokenProvider.getUsernameFromToken(token);
          // 스프링 시큐리티가 인증되었다고 인식할 수 있도록 인증 객체 생성
          UsernamePasswordAuthenticationToken authentication =
                  new UsernamePasswordAuthenticationToken(
                          username, null, Collections.emptyList()
                  );
          // 인증 객체를 현재 요청의 SecurityContext에 등록
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    } catch (Exception e) {
      logger.error("인증정보 설정이 없습니다.", e);
    }

    filterChain.doFilter(request, response);
  }
}
