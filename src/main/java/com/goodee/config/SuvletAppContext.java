package com.goodee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Spring MVC프로젝트에 관련된 설정을 하는 클래스
@Configuration
// Controller 어노테이션이 세팅되어 있는 클래스를 등록하는 어노테이션
@EnableWebMvc
// 스캔할 패키지 지정
@ComponentScan("com.goodee.controller")
public class SuvletAppContext implements WebMvcConfigurer{ 
	//WebMvcConfigurer : 웹MVC 스펙정보다 담긴 MVC를 제공함(즉 아래 오버라이드 한 메서드들) - 아래 오버라이드를 통해 재설정하는 것.
	
	// controller의 메서드가 반환하는 jsp의 이름 앞뒤에 경로와 확장자를 붙여주도록 설정한다.
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		WebMvcConfigurer.super.configureViewResolvers(registry);
		registry.jsp("/WEB-INF/views/",".jsp");  // 앞뒤로 확장자 넣어줌.(앞엔 WEB~로 시작하고 뒤는 .jsp로 끝남)
	}
	
	// 정적 파일의 경로 세팅
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		WebMvcConfigurer.super.addResourceHandlers(registry);
		registry.addResourceHandler("/resource/**").addResourceLocations("/resources/");  ///**:전체경로 아래 있는 모든 경로를 태울때(*는 모든 경로를 뜻함)~
		// 어떤 형태든 파일 앞이 resource면 이 경로를 태움(내부경로 resource폴더에 있는 경로를 태움)
		registry.addResourceHandler("/upload/**").addResourceLocations("file:///C:/sample/");
		// 만약 upload라고 경로가 시작되면 file:///C:/sample/에 있는 파일을 참조하겠다!(외부파일 경로를 태움)
	}
	
	// 파일 업로드 세팅
	private final int MAX_SIZE = 10*1024*1024;
	
	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		
		// 디폴트 인코딩 타입 설정
		multipartResolver.setDefaultEncoding("UTF-8");
		// 전체 올릴 수 있는 파일들의 총 용량 최대치
		multipartResolver.setMaxUploadSize(MAX_SIZE*10);
		// 파일 한개 당 올릴 수 있는 용량 최대치
		multipartResolver.setMaxUploadSizePerFile(MAX_SIZE);
		
		return multipartResolver;
	}
}
