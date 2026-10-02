package com.ksh.entity;

/*
기본 회원(Member) 테이블에 데이터가 있고
중 관리자 권한을 부여받은 사람만 어드민(Admin)
테이블에 조인(Join) 시켜서 관리자 전용으로 로그인

회원정보(비밀번호, 이름 등)는 Member에 일원화하고
Admin 테이블은 관리자 등급이나 부서같은 전용 데이터만
가지면서 Member를 부모로 참조(외래키)하는 것

JPA의 @OneToOne (또는 다대일) 조인을
사용하면 이 구조를 아주 우아하게 구현할 수 있음
*/

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admins")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Member 테이블의 id를 외래키(member_id)로 가져와서 조인
  @OneToOne(fetch = FetchType.LAZY) // 일대일관계
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @Column(length = 20)
  private String adminLevel;
  private String department;
}
