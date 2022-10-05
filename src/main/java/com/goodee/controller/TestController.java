package com.goodee.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.goodee.vo.MediaVO;

@Controller
public class TestController {
	
	// Spring file upload 정책
	/* - 스프링에서는 파일을 받기 위한 스펙을 제공하고 있으며, 그 중 하나가 MultipartFile 클래스에 바로
	 * 바이너리 파일을 넣는 형태로 제공한다.
	 * - 형식 : @RequestParam("type="file" input 이름") MultipartFile file
	 */
	@PostMapping("/test1")
	public String singleFileUpload(@RequestParam("mediaFile") MultipartFile file) throws IllegalStateException, IOException {
		
		 if(!file.getOriginalFilename().isEmpty()) {  // 파일이 제대로 있다면~
			 Path path = Paths.get("C:/sample/"+file.getOriginalFilename());
			 file.transferTo(path);
			 System.out.println("매우 잘 저장되었습니다.");
		 }else {
			 System.out.println("에러가 발생했습니다.");
		 }
		return "test1_result";
	}
	
	@PostMapping("/test2")
	public String multiFileUpload(@RequestParam("mediaFile") MultipartFile[] files) throws IllegalStateException, IOException{
		for(MultipartFile file : files) {
			 if(!file.getOriginalFilename().isEmpty()) {  // 파일이 제대로 있다면~
				 //Path path = Paths.get("C:/sample/"+file.getOriginalFilename());
				// file.transferTo(path);
				 file.transferTo(Paths.get("C:/sample/"+file.getOriginalFilename())); // 위를 짧게 변경(리팩토리)
				 System.out.println(file.getOriginalFilename() + "저장 완료.");
			 }else {
				 System.out.println("에러가 발생했습니다.");
			 }
		}
		return "test2_result";
	}
	
	@PostMapping("/test3")
	public String multiFileUpload(@RequestParam("mediaFile") MultipartFile[] files,
			@RequestParam String user, @RequestParam String url, Model model) 
			throws IllegalStateException, IOException{
		for(MultipartFile file : files) {
			 if(!file.getOriginalFilename().isEmpty()) {  // 파일이 제대로 있다면~
				 file.transferTo(Paths.get("C:/sample/"+file.getOriginalFilename())); // 위를 짧게 변경(리팩토리)
				 System.out.println(file.getOriginalFilename() + "저장 완료.");
			 }else {
				 System.out.println("에러가 발생했습니다.");
			 }
		}
		model.addAttribute("user",user);
		model.addAttribute("url",url);
		
		return "test3_result";
	}
	
	@PostMapping("/test4")
	public String multiFileUpload(MediaVO vo) throws IllegalStateException, IOException{
		MultipartFile[] files = vo.getMediaFile();
		for(MultipartFile file : files) {
			 if(!file.getOriginalFilename().isEmpty()) {  // 파일이 제대로 있다면~
				 file.transferTo(Paths.get("C:/sample/"+file.getOriginalFilename()));
				 System.out.println(file.getOriginalFilename() + "저장 완료.");
			 }else {
				 System.out.println("에러가 발생했습니다.");
			 }
		}
		return "test4_result";
	}
	
	
	@PostMapping("/test5")
	@ResponseBody  // 비동기이기때문에 추가
	public String multiFileUploadWithAjax(MultipartFile[] uploadfile) // index.jsp에 넣은 이름(uploadfile) 가져옴.
			throws IllegalStateException, IOException{
		for(MultipartFile file : uploadfile) {
			 if(!file.getOriginalFilename().isEmpty()) {  // 파일이 제대로 있다면~
				 //Path path = Paths.get("C:/sample/"+file.getOriginalFilename());
				// file.transferTo(path);
				 file.transferTo(Paths.get("C:/sample/"+file.getOriginalFilename())); // 위를 짧게 변경(리팩토리)
				 System.out.println(file.getOriginalFilename() + "저장 완료.");
			 }else {
				 System.out.println("에러가 발생했습니다.");
			 }
		}
		return "test5_received";
	}
	
	
	// 데이터 전송시에는 반드시 reponse 객체를 통해 전송을 해야 한다.
	@GetMapping("/download1")  //a태그라 get매핑
	public void download(HttpServletResponse response) throws Exception{  
	// String이 아닌 void로 해줘야 함.
		
		String path = "C:/sample/21_입출력.pdf";
		
		// 다운로드 받고나 하는 파일에 대한 Path 지정
		Path file = Paths.get(path);
		
		// 파일 이름 utf-8로 인코딩 : 파일 이름이 깨지지 않도록 설정하기 위함.
		String filename = URLEncoder.encode(file.getFileName().toString(), "UTF-8");
		
		
		// response 객체의 헤더 세팅( 이 이름으로 데이터를 받아옴. 헤더가 없으면 데이터를 제대로 받다 올 수 없음)
		response.setHeader("Context-Disposition", "attachment;filename="+file.getFileName());
		
		// 파일 channel 설정 : 받아오고자 하는 파일의 내용을 읽을 수 있는 형태(StandardOpenOption.READ)로 받아와 Channel로 설정함.
		// FileChannel fc : 이 채널을 단순히 가져오기 위한 형태로 씀.
		FileChannel fc = FileChannel.open(file, StandardOpenOption.READ);
		
		// response에서 output스트림 추출( OutputStream : 단방향 스트림)
		// 단방향 스트림을 getOutputStream으로 받아옴. 그냥 채널을 받아오지 않고 굳이 getOutputStream해서 아래에서 또 채널(outputChannel)을 받아올까?
		// 이유 : response에서는 지원이 되는 게 결국은 OutputStream만 뽑아내고 채널 형태로는 뽑아낼 수가 없음. 즉, 막바로 채널을 뽑아낼 수가 없음.
		// 때문에 어쩔수 없이 OutputStream을 뽑아내고 아래에서 채널로 변환해서 채널을 추출해야 함.
		OutputStream out = response.getOutputStream();
		
		// outputStream에서 channel 추출
		WritableByteChannel outputChannel = Channels.newChannel(out);
		
		// response 객체로 파일 전송
		// OutputStream, outputChannel 을 둘다 뽑아냈으니 이제 transferTo을 사용해서 사용자에게 요청할 수 있게됨.
		fc.transferTo(0, fc.size(), outputChannel);
	}
}
