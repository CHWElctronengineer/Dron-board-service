package com.example.dron.Service;

import com.example.dron.Entity.DroneImage;
import com.example.dron.Repository.DroneImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;


@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final DroneImageRepository droneImageRepository;

    public DroneImage store(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        // 파일 정보 추출
        String originalFilename = file.getOriginalFilename();
        String fileType = file.getContentType();

        // MultipartFile에서 byte 데이터를 직접 추출합니다.
        byte[] imageData = file.getBytes();

        // 파일 정보를 Entity에 저장 (경로 대신 byte 데이터)
        DroneImage image = new DroneImage();
        image.setOriginalFilename(originalFilename);
        image.setFileType(fileType);
        image.setImageData(imageData); // 바이트 데이터를 Entity에 설정

        return droneImageRepository.save(image);
    }

    // ID로 이미지를 삭제하는 메소드
    public void deleteImage(Long id) {
        // ID로 이미지가 존재하는지 확인
        if (!droneImageRepository.existsById(id)) {
            // 존재하지 않으면 예외를 발생시키거나, 그냥 넘어갈 수 있습니다.
            // 여기서는 간단히 로그만 남기고 종료하도록 처리합니다.
            System.out.println("삭제할 이미지를 찾을 수 없습니다. ID: " + id);
            return;
        }
        // 존재하면 DB에서 해당 데이터를 삭제합니다.
        droneImageRepository.deleteById(id);
    }


}
