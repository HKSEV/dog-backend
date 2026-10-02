package com.ksh.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity // 이 클래스가 데이터베이스의 테이블(Animal)과 1:1로 매핑됨을 선언
@Data // Getter/Setter/toString 메소드를 자동으로 생성
public class Animal {
    @Id
    // 이 변수를 데이터베이스 테이블의 기본키로 지정
    // 데이터가 추가될 때마다 DB가 알아서 번호를 1씩 증가시킴
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 동물 식별 고유 번호
    
    private String region, noticeNo, birthYear, gender, imageUrl, category;
    
    private Double weight;
}
