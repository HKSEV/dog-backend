package com.ksh.service;

import com.ksh.domain.PostStatus;
import com.ksh.dto.MissingPostRequestDto;
import com.ksh.dto.MissingPostResponseDto;
import com.ksh.entity.Member;
import com.ksh.entity.MissingPost;
import com.ksh.repository.MemberRepository;
import com.ksh.repository.MissingPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissingPostService {
  /*
  스프링에서 콩이란?
  Spring Bean : 스프링 컨테이너가 직접 만들고 관리하는 자바 객체
  자바에서 객체를 만들때 new 키워드로 직접 객체를 생성하고 소멸시키는 것과 달리
  스프링이(IoC 컨테이너)가 그 객체들의 생명주기(생성, 의존성 연결,소멸)을
  대신 관리

  스프링빈의 핵심 특징
  - 제어의 역전 : 객체의 제어권이 개발자가 아니라 스프링 컨테이너
  - 싱글톤(Singleton) 기본 제공 : 특별한 설정이 없다면 스프링은 빈을
  단 하나만 생성(싱글톤)하여 전파하고 재사용합니다. 메모리 낭비
  - 의존성 주입(DI): 빈과 빈 사이에 필요한 의존 관계를 스프링이 자동으로 연결해 줍니다

  자동 등록(@Component) vs 수동 등록(@Bean)
  두 방식은 객체를 스프링 빈으로 등록한다는 목적은 같지만,
  어디에 사용하고 어떻게 관리하느냐에서 큰 차이가 있습니다.

  자동 등록 (@Component)
  클래스 레벨 (클래스 선언부 위)
  내가 직접 작성한 비즈니스 로직 클래스
  스프링이 컴포넌트 스캔으로 자동 검색
  유연성 낮음 (하나의 클래스는 하나의 빈으로만 등록)

  수동 등록 (@Bean)
  메서드 레벨 (@Configuration 클래스 내부)
  외부 라이브러리 객체 또는 글로벌 설정
  개발자가 메서드로 직접 객체를 생성하여 반환
  유연성 높음 (조건에 따라 다른 객체를 반환하도록 제어 가능)

  • 자동 등록을 쓰는 경우: 내가 개발하는 서비스(@Service),
  컨트롤러(@Controller), 리포지토리(@Repository) 등
  일반적인 비즈니스 로직은 자동 등록을 기본으로 사용합니다.
  생산성이 높고 코드가 깔끔해집니다.

  • 수동 등록을 쓰는 경우: 외부 라이브러리(예: JWT 관련 객체,
   Security 설정, Querydsl 등)처럼 소스 코드를 수정할 수
   없는 클래스를 빈으로 등록해야 할 때 씁니다.
   또한 애플리케이션 전반에 걸쳐 공통적으로 적용되는
   기술 지원 객체를 명확하게 드러내고 싶을 때 사용합니다.
  */
  private final MissingPostRepository missingPostRepository;
  private final MemberRepository memberRepository;

  // 실종 신고글 작성
  @Transactional
  public MissingPostResponseDto createPost(
          MissingPostRequestDto requestDto, String username) {
    Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    MissingPost post = new MissingPost();

    post.setTitle(requestDto.getTitle());
    post.setContent(requestDto.getContent());
    post.setBreed(requestDto.getBreed());
    post.setGender(requestDto.getGender());
    post.setAge(requestDto.getAge());
    post.setWeight(requestDto.getWeight());
    post.setColor(requestDto.getColor());
    post.setRescueLocation(requestDto.getRescueLocation());
    post.setMediaUrls(requestDto.getMediaUrls());
    post.setStatus(PostStatus.MISSING);
    post.setAuthor(member);
    post.setCreatedAt(LocalDateTime.now());

    MissingPost savePost = missingPostRepository.save(post);
    return new MissingPostResponseDto(savePost);
  }

  // 무한 스크롤 조회
  public List<MissingPostResponseDto> getPostsByScroll(Long cursorId, int size) {
    Pageable pageable = PageRequest.of(0, size);
    List<MissingPost> posts = missingPostRepository.findAllByCursor(cursorId, pageable);
    return posts.stream().map(MissingPostResponseDto::new)
            .collect(Collectors.toList());
  }

  // 글 수정(본인확인)
  @Transactional
  public MissingPostResponseDto updatePost(
          Long id, MissingPostRequestDto requestDto, String username) {
    MissingPost post = missingPostRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

    if (!post.getAuthor().getUsername().equals(username)) {
      throw new SecurityException("수정 권한이 없습니다.");
    }

    // 수정 내용 반영
    post.setTitle(requestDto.getTitle());
    post.setContent(requestDto.getContent());
    post.setBreed(requestDto.getBreed());
    post.setGender(requestDto.getGender());
    post.setAge(requestDto.getAge());
    post.setWeight(requestDto.getWeight());
    post.setColor(requestDto.getColor());
    post.setRescueLocation(requestDto.getRescueLocation());
    post.setMediaUrls(requestDto.getMediaUrls());
    post.setUpdatedAt(LocalDateTime.now());

    return new MissingPostResponseDto(post);
  }

  // 글 삭제(본인확인)
  @Transactional
  public void deletePost(Long id, String username) {
    MissingPost post = missingPostRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

    if (!post.getAuthor().getUsername().equals(username)) {
      throw new SecurityException("삭제 권한이 없습니다.");
    }

    missingPostRepository.delete(post);
  }

  // 완료 처리
  @Transactional
  public void completePost(Long id, String username) {
    MissingPost post = missingPostRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

    if (!post.getAuthor().getUsername().equals(username)) {
      throw new SecurityException("권한이 없습니다.");
    }
  }
}
