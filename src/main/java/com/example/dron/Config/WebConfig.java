package com.example.dron.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Web MVC 관련 사용자 정의 설정을 위한 클래스입니다.
 * WebMvcConfigurer 인터페이스를 구현하여 CORS 정책, 정적 리소스 처리 등
 * 웹 관련 설정을 프로젝트의 요구사항에 맞게 추가하거나 변경합니다.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // application.properties 파일에 정의된 'file.upload-dir' 값을 주입받습니다.
    // 이 변수에는 업로드된 파일이 저장될 실제 서버의 폴더 경로가 담겨있습니다.
    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 정적 리소스(static resource)를 처리하는 핸들러를 등록하는 메소드입니다.
     * 이 설정을 통해 특정 URL 경로로 오는 요청을 실제 파일 시스템의 특정 폴더로 연결하여,
     * 이미지, CSS, JS 파일 등을 웹에서 직접 접근할 수 있게 만들어줍니다.
     * @param registry 리소스 핸들러를 등록하기 위한 레지스트리 객체
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // "/images/**" URL 패턴으로 요청이 오면,
        registry.addResourceHandler("/images/**")
                // 로컬 디스크의 uploadDir 경로에서 파일을 찾아 제공하도록 설정합니다.
                // "file:" 접두사는 URL이 아닌 실제 파일 시스템의 경로임을 명시하는 프로토콜입니다.
                // 예: /images/photo.jpg 요청 -> C:/uploads/drone-images/photo.jpg 파일을 찾아 반환
                .addResourceLocations("file:" + uploadDir);
    }

    /**
     * 애플리케이션 전역의 CORS(Cross-Origin Resource Sharing) 정책을 설정하는 메소드입니다.
     * 다른 출처(Origin)의 웹 애플리케이션(예: 포트가 다른 리액트 앱)이
     * 이 서버의 API를 안전하게 호출할 수 있도록 허용 규칙을 정의합니다.
     * @param registry CORS 매핑을 등록하기 위한 레지스트리 객체
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // "/api/"로 시작하는 모든 경로에 대해 이 CORS 정책을 적용
                .allowedOrigins("http://localhost:5176", "http://192.168.0.141:5176") // 요청을 허용할 프론트엔드 앱의 주소 목록
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 허용할 HTTP 메소드
                .allowedHeaders("*") // 모든 종류의 요청 헤더를 허용
                .allowCredentials(true); // 인증 정보(쿠키, Authorization 헤더 등)를 포함한 요청 허용
    }
}