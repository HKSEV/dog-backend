package com.ksh.service;

import com.ksh.dto.ShelterAnimalRequestDto;
import com.ksh.entity.ShelterAnimal;
import com.ksh.repository.ShelterAnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service // 서비스 역할 알려줌
@RequiredArgsConstructor
public class ShelterAnimalService {
  private final ShelterAnimalRepository repository;

  // 작업 중 하나라도 실패하면 원상태로 롤백 가능
  @Transactional
  public void registerAnimal(ShelterAnimalRequestDto dto) {
    // 최종적으로 DB에 저장할 이미지 주소를 담을 변수
    // 사용자가 폼에서 "LINK"로 입력했을 경우를 대비해 우선 그 값으로 초기화
    String finalImageUrl = dto.getImageUrl();
    // 프론트엔드에서 폼 데이터로 넘어온 파일 객체를 꺼냄
    MultipartFile file = dto.getImageFile();
    // 실제로 존재하고 비어있지 않는 파일일 경우에만 저장 로직 실행
    if (file != null && !file.isEmpty()) {
      try {
        // 현재 실행 중인 프로젝트의 최상단 경로(user.dir)를 가져와
        // "/uploads"라는 폴더 경로를 저장
        String projectPath = System.getProperty("user.dir") + "/uploads";
        // 위에서 만든 경로를 바탕으로 File 객체(폴더)를 생성
        File uploadDir = new File(projectPath);
        // 만약 해당 경로에 uploads 폴더가 아직 존재하지 않는다면
        if (!uploadDir.exists()) {
          uploadDir.mkdirs();
        }
        // 사용자가 올린 파일의 원래 이름(예: 강아지.jpg)을 가져옴
        String originalFilename = file.getOriginalFilename();
        // 파일 이름이 중복되어 기존 파일이 덮어씌워지는 것을 방지하기 위해
        // 유일한 무작위 문자열 생성(UUID)
        String uuid = UUID.randomUUID().toString();
        // 무작위 문자열과 원래 이름을 합쳐서 저장할 새이름 만듦
        String savedFileName = uuid + "_" + originalFilename;
        // 최종 저장할 폴더 위치와 새 파일명을 합쳐서 파일 저장용 객체 만듦
        File saveFile = new File(uploadDir, savedFileName);
        // 메모리에 임시로 올라와 있던 업로드된 파일을 실제로 물리적 경로에 씀
        file.transferTo(saveFile);
        // 서버에 실제 파일 저장이 성공했으니
        // DB에는 웹에서 해당 이미지를 불러올 수 있는 접근 경로로 덮어씌움
        finalImageUrl = "/uploads/" + savedFileName;
      } catch (IOException e) {
        throw new RuntimeException("파일 업로드중 오류가 발생했습니다.");
      }
    }
    // dto -> entity 변환 및 DB 저장
    ShelterAnimal animal = ShelterAnimal.builder()
      .status(dto.getStatus())
      .gender(dto.getGender())
      .breed(dto.getBreed())
      .noticeNo(dto.getNoticeNo())
      .regDate(dto.getRegDate())
      .rescueLocation(dto.getRescueLocation())
      .content(dto.getContent())
      .imageUrl(finalImageUrl)
      .build();

    repository.save(animal);
  }

  public List<ShelterAnimal> getAllAnimals() {
    return repository.findAll();
  }
}
