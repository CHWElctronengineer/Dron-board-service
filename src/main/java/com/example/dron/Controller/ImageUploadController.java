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

/**
 * 드론 이미지와 관련된 모든 CRUD(Create, Read, Update, Delete) 요청을 처리하는 REST 컨트롤러입니다.
 * '/api/images' 경로에 대한 요청을 담당합니다.
 */
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageUploadController {

    private final ImageUploadService imageUploadService;
    private final DroneImageRepository droneImageRepository;

    /**
     * 새로운 이미지를 업로드(생성)하는 API입니다.
     * @param file 이미지 파일 (multipart/form-data)
     * @param locationId 위치 ID
     * @param processId 공정 ID
     * @return 성공 시 생성된 이미지의 ID와 HTTP 201 Created 상태를 반환합니다.
     */
    @PostMapping("/upload")
    // ✅ @RequestParam을 사용하여 locationId와 processId를 추가로 받습니다.
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
                                         @RequestParam("locationId") Integer locationId,
                                         @RequestParam("processId") String processId) {
        try {
            // ✅ 수정한 서비스 메서드에 모든 파라미터를 전달합니다.
            DroneImage savedImage = imageUploadService.store(file, locationId, processId);
            return ResponseEntity.status(HttpStatus.CREATED).body("업로드 성공! 이미지 ID: " + savedImage.getId());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패: " + e.getMessage());
        }
    }

    /**
     * ID를 이용해 특정 이미지의 실제 파일 데이터를 조회하는 API입니다. (변경 없음)
     */
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        DroneImage image = droneImageRepository.findById(id).orElse(null);

        if (image == null || image.getImageData() == null || image.getFileType() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(image.getFileType()))
                .body(image.getImageData());
    }

    /**
     * 업로드된 모든 이미지의 정보 목록을 조회하는 API입니다.
     * @return 이미지 정보 DTO 리스트 (location_id, process_id 포함)
     */
    @GetMapping
    public ResponseEntity<List<ImageInfoDTO>> getAllImages() {
        List<ImageInfoDTO> images = droneImageRepository.findAll().stream()
                .map(image -> new ImageInfoDTO(
                        image.getId(),
                        image.getOriginalFilename(),
                        image.getFileType(),
                        // ✅ DTO 생성자에 새로 추가한 필드 값을 전달합니다.
                        image.getLocation_id(),
                        image.getProcess_id()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(images);
    }

    /**
     * ID를 이용해 특정 이미지를 삭제하는 API입니다. (변경 없음)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        imageUploadService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}