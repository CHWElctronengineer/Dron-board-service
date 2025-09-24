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
@Service // Spring: 이 클래스가 비즈니스 로직을 담당하는 서비스 계층의 컴포넌트임을 나타냅니다.
@RequiredArgsConstructor // Lombok: final 키워드가 붙은 필드를 포함하는 생성자를 자동으로 만들어줍니다. (의존성 주입)
public class ImageUploadService {

    // final로 선언하여 불변성을 보장하고, @RequiredArgsConstructor를 통해 의존성을 주입받습니다.
    private final DroneImageRepository droneImageRepository;

    /**
     * MultipartFile 객체를 받아 파일 데이터를 데이터베이스에 저장합니다.
     * @param file 컨트롤러에서 전달받은 업로드 파일
     * @return 데이터베이스에 저장된 이미지 엔티티 객체
     * @throws IOException 파일 처리 중 발생할 수 있는 입출력 예외
     */
    public DroneImage store(MultipartFile file) throws IOException {
        // 파일이 비어있는 경우, 아무 처리도 하지 않고 null을 반환합니다.
        if (file.isEmpty()) {
            return null;
        }

        // --- 파일 정보 추출 ---
        // 파일의 원본 이름을 가져옵니다. (예: "photo.jpg")
        String originalFilename = file.getOriginalFilename();
        // 파일의 MIME 타입을 가져옵니다. (예: "image/jpeg")
        String fileType = file.getContentType();
        // 파일의 실제 데이터(바이트 배열)를 가져옵니다.
        byte[] imageData = file.getBytes();

        // --- 엔티티 생성 및 데이터 설정 ---
        // 새로운 DroneImage 엔티티 객체를 생성합니다.
        DroneImage image = new DroneImage();
        // 파일의 원본 이름을 엔티티에 설정합니다.
        image.setOriginalFilename(originalFilename);
        // 파일의 타입을 엔티티에 설정합니다.
        image.setFileType(fileType);
        // 파일의 바이트 데이터를 엔티티에 설정합니다.
        image.setImageData(imageData);

        // JpaRepository의 save 메소드를 호출하여 엔티티를 데이터베이스에 저장하고,
        // 저장된 엔티티를 반환합니다.
        return droneImageRepository.save(image);
    }

    /**
     * 이미지의 ID를 받아 데이터베이스에서 해당 이미지를 삭제합니다.
     * @param id 삭제할 이미지의 고유 ID
     */
    public void deleteImage(Long id) {
        // 주어진 ID로 데이터가 존재하는지 먼저 확인합니다.
        if (!droneImageRepository.existsById(id)) {
            // 존재하지 않으면, 콘솔에 메시지를 출력하고 메소드를 종료합니다.
            // 실제 애플리케이션에서는 로깅 라이브러리를 사용하거나 사용자 정의 예외를 던지는 것이 좋습니다.
            System.out.println("삭제할 이미지를 찾을 수 없습니다. ID: " + id);
            return;
        }
        // JpaRepository의 deleteById 메소드를 사용하여 ID에 해당하는 데이터를 삭제합니다.
        droneImageRepository.deleteById(id);
    }
}