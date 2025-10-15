package com.example.dron.Service;

import com.example.dron.Entity.DroneImage;
import com.example.dron.Repository.DroneImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * 이미지 파일의 업로드, 저장, 삭제 등 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final DroneImageRepository droneImageRepository;

    /**
     * MultipartFile 객체와 추가 데이터를 받아 파일 정보를 데이터베이스에 저장합니다.
     * @param file 컨트롤러에서 전달받은 업로드 파일
     * @param locationId 이미지가 촬영된 위치 ID (1~6)
     * @param processId 이미지가 속한 공정 ID
     * @return 데이터베이스에 저장된 이미지 엔티티 객체
     * @throws IOException 파일 처리 중 발생할 수 있는 입출력 예외
     */
    // ✅ 메서드 시그니처에 locationId, processId 파라미터를 추가합니다.
    public DroneImage store(MultipartFile file, Integer locationId, String processId) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        // --- 엔티티 생성 및 데이터 설정 ---
        DroneImage image = new DroneImage();

        // 기존 파일 정보 설정
        image.setOriginalFilename(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        image.setImageData(file.getBytes());

        // ✅ 새로 추가된 위치 및 공정 ID를 엔티티에 설정합니다.
        image.setLocation_id(locationId);
        image.setProcess_id(processId);

        // JpaRepository의 save 메소드를 호출하여 엔티티를 데이터베이스에 저장합니다.
        return droneImageRepository.save(image);
    }

    /**
     * 이미지의 ID를 받아 데이터베이스에서 해당 이미지를 삭제합니다.
     * @param id 삭제할 이미지의 고유 ID
     */
    public void deleteImage(Long id) {
        if (!droneImageRepository.existsById(id)) {
            // 실제 애플리케이션에서는 로깅 라이브러리를 사용하는 것이 좋습니다.
            System.out.println("삭제할 이미지를 찾을 수 없습니다. ID: " + id);
            return;
        }
        droneImageRepository.deleteById(id);
    }
}