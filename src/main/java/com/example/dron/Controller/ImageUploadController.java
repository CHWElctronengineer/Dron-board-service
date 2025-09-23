package com.example.dron.Controller;


import com.example.dron.Dto.ImageInfoDTO;
import com.example.dron.Entity.DroneImage;
import com.example.dron.Repository.DroneImageRepository;
import com.example.dron.Service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageUploadController {

    private final ImageUploadService imageUploadService;
    private final DroneImageRepository droneImageRepository;

    // 이미지 업로드 API (기존과 거의 동일)
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            DroneImage savedImage = imageUploadService.store(file);
            // 성공 응답으로 이미지의 id만 반환해주는 것이 깔끔합니다.
            return ResponseEntity.status(HttpStatus.CREATED).body("업로드 성공! 이미지 ID: " + savedImage.getId());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패: " + e.getMessage());
        }
    }

    // DB에 저장된 이미지를 ID로 조회하여 보여주는 API (새로 추가)
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        // 1. DB에서 이미지 정보를 찾습니다.
        DroneImage image = droneImageRepository.findById(id).orElse(null);

        // 2. 이미지가 없거나, 이미지 데이터 또는 타입 정보가 없으면 404 에러를 보냅니다.
        if (image == null || image.getImageData() == null || image.getFileType() == null) {
            return ResponseEntity.notFound().build();
        }

        // ✅ [디버깅 로그 추가] 실제 응답을 보내기 직전의 Content-Type을 확인합니다.
        MediaType mediaType = MediaType.valueOf(image.getFileType());
        System.out.println(">>> [드론 서버] 응답 직전 Content-Type: " + mediaType);

        // 3. ✅ DB에 저장된 fileType('image/png' 등)을 Content-Type 헤더로 설정하고,
        //    ✅ imageData(byte[])를 응답 본문으로 보냅니다.
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(image.getFileType()))
                .body(image.getImageData());
    }

    @GetMapping
    public ResponseEntity<List<ImageInfoDTO>> getAllImages() {
        List<ImageInfoDTO> images = droneImageRepository.findAll().stream()
                .map(image -> new ImageInfoDTO(
                        image.getId(),
                        image.getOriginalFilename(),
                        image.getFileType()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(images);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        imageUploadService.deleteImage(id);
        // 성공적으로 삭제되었음을 의미하는 204 No Content 상태를 반환합니다.
        return ResponseEntity.noContent().build();
    }
}
