package com.example.dron.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "drone_image")
public class DroneImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFilename;
    private String fileType;

    @Lob // 이 필드가 Large Object(BLOB, CLOB)임을 명시합니다.
    @Column(columnDefinition = "LONGBLOB") // DB 컬럼 타입을 명확히 지정합니다.
    private byte[] imageData; // 파일 데이터는 byte 배열로 관리합니다.

    @CreationTimestamp
    private LocalDateTime createdAt;
}