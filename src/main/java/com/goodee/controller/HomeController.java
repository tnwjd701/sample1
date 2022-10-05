package com.goodee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller     //나는 컨트롤러에요 라고 알려주기 위해 @Controller 작성(서블릿대신)
public class HomeController {
	
	@RequestMapping(value = "/")  // 매핑해주는 어노테이션  // 경로에 /를 붙이겠다는 뜻
	public String home() {
		System.out.println("HomeController.home()");
		// 오는지 안오는지 확인용
		return "index";
		// index는 suvlet-context.xml내 beans 내 value가 뜻하는 걸 가리킴.
	}
}
