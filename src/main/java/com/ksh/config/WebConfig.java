package com.ksh.config;

import com.ksh.security.JwtAuthenticationFilter;
import com.ksh.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebConfig implements WebMvcConfigurer {
  @Override
  // 정적 리소스(이미지, 영상, HTML 등)를 웹에서 어떻게 접근할지 연결해 주는 역할
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/uploads/**").addResourceLocations(
      "file:///" + System.getProperty("user.dir") + "/uploads/"
    );
  }

  // CORS
  // 💡 기존 addCorsMappings를 지우고, 시큐리티가 100% 신뢰하는 전용 CORS 빈으로 변경했습니다.
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("http://localhost:3000"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config); // 모든 경로(/**)에 위 규칙 적용
    return source;
  }

  @Bean
  public SecurityFilterChain filterChain(
    HttpSecurity http, JwtTokenProvider tokenProvider
  ) throws Exception {
    http.cors(Customizer.withDefaults())
      .csrf(csrf -> csrf.disable())
      .addFilterBefore(
        new JwtAuthenticationFilter(tokenProvider),
        org.springframework.security.web.authentication
          .UsernamePasswordAuthenticationFilter.class
      )
      .authorizeHttpRequests(
        auth -> auth.anyRequest().permitAll()
      );
    return http.build();
  }
}
