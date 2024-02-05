
package com.picstory.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 설정파일을 스캔하기 위해 붙이는 어노테이션
public class CorsConfig implements WebMvcConfigurer {
	// Spring Boot에서 설정들을 구현하기 편하게 인터페이스 제공
	
	// 직면한 문제에 따라서, 메소드를 오버라이딩해서 설정
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // 어떤 URL에 대해서 설정을 할 것인지 / **<-모든 요청에 대해서
				.allowedOrigins("*")
				.allowedMethods("GET", "POST"); // 어떤 사용자들에게 요청을 허용하는지
	}
	

}
