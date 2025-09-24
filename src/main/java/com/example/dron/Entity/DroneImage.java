package com.example.dron.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 드론으로 촬영한 이미지 파일 정보를 데이터베이스에 저장하기 위한 엔티티 클래스입니다.
 * 'drone_image' 테이블과 매핑됩니다.
 */
@Data // Lombok: Getter, Setter, toString(), equals(), hashCode() 메서드를 자동으로 생성합니다.
@Entity // JPA: 이 클래스가 데이터베이스 테이블과 매핑되는 엔티티임을 선언합니다.
@Table(name = "drone_image") // JPA: 엔티티와 매핑될 테이블의 이름을 'drone_image'로 지정합니다.
public class DroneImage {

    /**
     * 이미지의 고유 식별자 (Primary Key) 입니다.
     */
    @Id // JPA: 이 필드가 테이블의 기본 키(Primary Key)임을 명시합니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA: 기본 키 값을 데이터베이스가 자동으로 생성(auto-increment)하도록 설정합니다.
    private Long id;

    /**
     * 업로드된 이미지 파일의 원래 이름입니다.
     * 예: "drone_photo_1.jpg"
     */
    private String originalFilename;

    /**
     * 파일의 MIME 타입입니다.
     * 예: "image/jpeg", "image/png"
     */
    private String fileType;

    /**
     * 이미지 파일의 실제 데이터입니다.
     * byte 배열 형태로 데이터베이스에 저장됩니다.
     */
    @Lob // JPA: 이 필드가 Large Object(대용량 객체)임을 명시합니다. (BLOB 또는 CLOB)
    @Column(columnDefinition = "LONGBLOB") // JPA & Hibernate: 데이터베이스 컬럼의 실제 타입을 'LONGBLOB'으로 지정하여 매우 큰 바이너리 데이터를 저장할 수 있게 합니다.
    private byte[] imageData;

    /**
     * 데이터가 처음 생성된 시간입니다.
     * 레코드가 생성될 때 자동으로 현재 시간이 기록됩니다.
     */
    @CreationTimestamp // Hibernate: 엔티티가 생성될 때의 타임스탬프를 자동으로 기록합니다.
    private LocalDateTime createdAt;
}