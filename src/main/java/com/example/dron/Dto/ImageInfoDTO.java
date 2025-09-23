package com.example.dron.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageInfoDTO {
    private Long id;
    private String originalFilename;
    private String fileType;
}
