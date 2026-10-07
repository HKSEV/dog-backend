package com.ksh.controller;

import com.ksh.entity.Member;
import com.ksh.repository.MemberRepository;
import com.ksh.security.JwtTokenProvider;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// 내부적으로 @Controller와 @ResponseBody가 합쳐진 형태
@RestController
// 이 클래스 내의 모든 API 주소는 기본적으로 "/api/members"로 시작하도록 설정
@RequestMapping("/api/members") // 모든 API 주소의 공통 접두사를 설정
@CrossOrigin(origins = "http://localhost:3000") // 교차 출처 리소스 공유
public class MemberController { // 외부에서 접근 가능한 컨트롤러 클래스 시작
  // DB와 소통하는 Repository를 담을 불변 객체
  private final MemberRepository memberRepository; // 불변성 설정
  private final JwtTokenProvider jwtTokenProvider;

  // 스프링부트가 실행될 때 자동으로 Repository를 연결해주는 생성자
  public MemberController(
          MemberRepository memberRepository,
          JwtTokenProvider jwtTokenProvider
  ) {
    this.memberRepository = memberRepository;
    this.jwtTokenProvider = jwtTokenProvider;
  } // 객체를 알아서 메모리에 생성해 두었다가 이 컨트롤러가 생성될 때 집어넣음
  
  // 1.중복체크 API
  // /api/members/check-email?email=
  @GetMapping("/check-email")
  // @RequestParam: URL 뒤에 쿼리스트링으로 넘어온 값(?email=abc@...)을
  // 뽑아내어 자바의 String email 변수에 담아줌
  public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
    return ResponseEntity.ok(memberRepository.existsByEmail(email));
  }
  // 2.중복체크 API 닉네임
  @GetMapping("/check-nickname")
  public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
    return ResponseEntity.ok(memberRepository.existsByNickname(nickname));
  }

  @PostMapping("/signup")
  public ResponseEntity<Member> signup(@RequestBody Member member) {
    // 이 앱은 가입 시 카카오와 일반을 선택하는데 그에 대한 문제를 먼저 클리어
    if (member.getProvider() == null || member.getProvider().isEmpty())
      member.setProvider("LOCAL");
      /* OAuth이 아닌 프론트엔드에서 provider(가입경로) 데이터를 넘기지 않았거나 비어있다면
       * 일반적인 자체 회원가입으로 간주하여 "LOCAL"이라는 값을 강제로 세팅
       * 데이터베이스 에러를 막기위한 방어 로직 */
    Member savedMember = memberRepository.save(member);
    // 부트는 기존 복잡한 SQL 쿼리문이 아닌 .save()로 스프링이 알아서 쿼리를 만들어 DB에 저장,
    // 저장된 결과를 savedMember에 다시 담아줌
    return ResponseEntity.status(HttpStatus.CREATED).body(savedMember);
    // 저장이 성공적으로 완료되었으면 200(단순성공) 대신 더 명확한 201(새로운 리소스가 생성)
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Member loginData) {
    /* 사용자가 로그인 창에 입력한 이메일로 DB를 검색
     * 만약 가입되지 않은 이메일이면 데이터가 없을텐데
     * 이때 에러가 터지지 않도록 Optional이라는 상자에 결과를 담음 */
    Optional<Member> memberOpt = memberRepository.findByEmail(loginData.getEmail());

    if (memberOpt.isPresent()) {
      Member member = memberOpt.get();
      /* isPresent(): 상자 안에 데이터가 들어있나? 라고 질문
       * .get(): 데이터가 있다면 상자에서 실제 Member 객체를 꺼냄 */
      if (member.getPassword().equals(loginData.getPassword())) {
        // 로그인 성공 시 사용자의 name(또는 email)을 기반으로 JWT 토큰 생성
        // JwtTokenProvider에서 getSubject()로 name을 쓰도록 설정되어 있으므로
        // member.getName()을 넣음
        String token = jwtTokenProvider.createToken(member.getName());

        // 프론트엔드가 필요로 하는 정보(닉네임, 토큰 등)를 담은 Map 반환
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("nickname", member.getNickname());
        responseMap.put("name", member.getName());
        responseMap.put("email", member.getEmail());
        responseMap.put("token", token);

        // DB의 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 검사
        return ResponseEntity.ok(responseMap);
        // 비밀번호가 맞다면 로그인 성공, 회원 정보를 프론트로 넘겨줌
      }
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일 또는 비밀번호가 일치하지 않습니다");
    // 회원이 없거나 비밀번호가 틀리면 401(인증 실패) 에러 메시지를 보냄
  }

  @PostMapping("/upload-profile")
  public ResponseEntity<String> uploadProfile(@RequestParam("file") MultipartFile file) {
    try {
      // 1.파일 이름이 겹치지 않도록 현재 시간(밀리초)을 앞에 붙여줌
      String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
      // 2.프로젝트 폴더 안의 "uploads"라는 폴더에 저장할 경로를 잡음
      String uploadDir = System.getProperty("user.dir") + "/uploads/";
      Path path = Paths.get(uploadDir + filename);
      // 3.폴더가 없으면 만들고, 파일을 복사해서 사용
      Files.createDirectories(path.getParent());
      Files.write(path, file.getBytes());
      // 4.저장된 이미지에 접근할 수 있는 가짜 URL을 프론트로 반환
      String imageUrl = "http://localhost:8080/uploads/" + filename;

      return ResponseEntity.ok(imageUrl);
    }
    catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(
              HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 실패");
    }
  }
}
