package com.ksh.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// 카카오 가입자는 이메일이 없음 수 있음
	// nullable = ture 혹은 소셜 로그인 아이디를 저장할 컬럼 추가
	@Column(unique = true)
	private String email;

	@Column(unique = true, nullable = false)
	private String nickname;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String phone;

	@Column(length = 500)
	private String address;

	@Column(nullable = false)
	private String userType;

	private boolean marketingAgreed;
	// 가입 경로(LOCAL: 일반가입, KAKAO: 카카오 가입)
	private String provider;
	// 카카오에서 넘겨주는 고유 회원번호
	private String providerId;

	private String profileImageUrl;

	public String getUsername() {
		return this.name;
	}
}
