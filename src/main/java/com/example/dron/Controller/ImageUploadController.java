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

    // 이미지 업로드 및 삭제와 같은 비즈니스 로직을 처리하는 서비스
    private final ImageUploadService imageUploadService;
    // 데이터베이스와 직접 상호작용하여 데이터를 조회하는 리포지토리
    // 일반적으로 컨트롤러는 서비스만 호출하지만, 간단한 조회의 경우 리포지토리를 직접 사용할 수도 있습니다.
    private final DroneImageRepository droneImageRepository;

    /**
     * 새로운 이미지를 업로드(생성)하는 API입니다. (HTTP POST)
     * @param file 클라이언트로부터 전송된 이미지 파일 (multipart/form-data 형식)
     * @return 성공 시 생성된 이미지의 ID와 HTTP 201 Created 상태를 반환합니다.
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            DroneImage savedImage = imageUploadService.store(file);
            // 성공 응답으로 이미지의 id만 반환해주는 것이 깔끔합니다.
            return ResponseEntity.status(HttpStatus.CREATED).body("업로드 성공! 이미지 ID: " + savedImage.getId());
        } catch (IOException e) {
            e.printStackTrace();
            // 파일 처리 중 오류 발생 시 500 에러를 반환합니다.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패: " + e.getMessage());
        }
    }

    /**
     * ID를 이용해 특정 이미지의 실제 파일 데이터를 조회하는 API입니다. (HTTP GET)
     * @param id 조회할 이미지의 고유 ID (URL 경로 변수)
     * @return 이미지의 바이너리 데이터(byte[])와 Content-Type 헤더를 포함하는 응답
     */
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        // 1. DB에서 ID를 기반으로 이미지 정보를 찾습니다. 없으면 null을 반환합니다.
        DroneImage image = droneImageRepository.findById(id).orElse(null);

        // 2. 이미지가 존재하지 않거나, 이미지 데이터 또는 파일 타입 정보가 없으면 404 Not Found 에러를 보냅니다.
        if (image == null || image.getImageData() == null || image.getFileType() == null) {
            return ResponseEntity.notFound().build();
        }

        // 디버깅용 로그: 응답을 보내기 직전의 Content-Type을 콘솔에 출력합니다.
        MediaType mediaType = MediaType.valueOf(image.getFileType());
        System.out.println(">>> [드론 서버] 응답 직전 Content-Type: " + mediaType);

        // 3. 응답 헤더에 정확한 이미지 타입(Content-Type)을 설정하고,
        //    응답 본문(body)에 이미지의 실제 바이트 데이터를 담아 클라이언트에 전송합니다.
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(image.getImageData());
    }

    /**
     * 업로드된 모든 이미지의 정보 목록을 조회하는 API입니다. (HTTP GET)
     * @return 이미지 정보(ID, 파일명 등)가 담긴 DTO 객체 리스트
     */
    @GetMapping
    public ResponseEntity<List<ImageInfoDTO>> getAllImages() {
        // DB에서 모든 이미지 엔티티를 조회합니다.
        List<ImageInfoDTO> images = droneImageRepository.findAll().stream()
                // 전체 이미지 데이터를 보내면 매우 비효율적이므로,
                // ID, 파일명 등 가벼운 정보만 담은 DTO로 변환하여 반환합니다.
                .map(image -> new ImageInfoDTO(
                        image.getId(),
                        image.getOriginalFilename(),
                        image.getFileType()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(images);
    }

    /**
     * ID를 이용해 특정 이미지를 삭제하는 API입니다. (HTTP DELETE)
     * @param id 삭제할 이미지의 고유 ID
     * @return 성공적으로 삭제되었음을 의미하는 204 No Content 상태 응답
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        imageUploadService.deleteImage(id);
        // 리소스가 성공적으로 삭제되었지만, 별도의 응답 본문은 없음을 알립니다.
        return ResponseEntity.noContent().build();
    }
}