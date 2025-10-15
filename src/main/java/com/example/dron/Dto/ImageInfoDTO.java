package com.example.dron.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 이미지 목록을 클라이언트에 전송할 때 사용하는 DTO(Data Transfer Object)입니다.
 * 전체 이미지 데이터(byte[])를 모두 보내는 것은 네트워크에 큰 부담을 주므로,
 * 갤러리 뷰를 구성하는 데 필요한 최소한의 정보(ID, 파일명, 파일 타입)만 담아서 보내기 위해 사용됩니다.
 *
 * @Data: Lombok 어노테이션으로, getter, setter, toString 등의 메소드를 자동으로 생성합니다.
 * @AllArgsConstructor: 모든 필드를 파라미터로 받는 생성자를 자동으로 생성합니다.
 * Controller에서 Entity를 DTO로 변환할 때 new ImageInfoDTO(...) 형태로 편리하게 사용됩니다.
 */
@Data
@AllArgsConstructor
public class ImageInfoDTO {

    /**
     * 이미지의 고유 식별자 (Primary Key)
     */
    private Long id;

    /**
     * 사용자가 업로드한 원본 파일의 이름
     */
    private String originalFilename;

    /**
     * 이미지의 MIME 타입 (예: "image/png", "image/jpeg")
     * 이 정보는 클라이언트가 데이터를 어떻게 처리할지 결정하는 데 사용될 수 있습니다.
     */
    private String fileType;


    /**
     * 이미지가 촬영된 위치 식별자 (1~6)
     * DroneImage 엔티티에 새로 추가된 필드입니다.
     */
    private Integer location_id;

    /**
     * 이미지가 속한 공정 식별자
     * DroneImage 엔티티에 새로 추가된 필드입니다.
     */
    private String process_id;
}