package edu.kh.fin.common.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // 모든 클래스에서 발생하는 예외를 모아서 처리하는 클래스
public class ExceptionController {

	// 어떤 예외를 처리할 것인가를 지정하는 어노테이션
	// Exception.class == 모든 예외의 최상위 부모 
	//                 == 모든 예외를 해당 메소드로 처리
	@ExceptionHandler(Exception.class) 
	public String exceptionHandler(Exception e, Model model) {
		
		e.printStackTrace(); // 콘솔에 에러 내용과 발생한 메소드 순서를 출력
		model.addAttribute("errorMsg", "서비스 이용 중 문제가 발생했습니다.");
		
		// Model : 데이터를 맵형식(k:v)으로 전달하는 객체 (기본 request scope)
		
		return "common/error";
	}
	
	
	
}




