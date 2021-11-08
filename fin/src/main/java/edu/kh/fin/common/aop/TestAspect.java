package edu.kh.fin.common.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component // bean 등록 == IOC에 의해 스프링이 제어
@Aspect 
public class TestAspect {
	
	// @Aspect : @Aspect 어노테이션이 적용된 클래스는
	// 런타임 시 필요한 위치에 동적으로 참여하게 되는데 이를 Spring 컨테이너가 수행함.
	// -> 이 때 spring 컨테이너가 이를 제어하기 위해서 bean으로 등록되어 있어야 한다.
	
	// Aspect (관점) : 공통 관심사(중복되서 많이 실행되는 코드)를 작성하는 클래스
	//                -> 해당 클래스에 advice, Pointcut이 작성됨
	
	
	// Join Point : 관점의 대상이 될수 있는 부분
	// Pointcut : Join Point 중 advice가 실행될 부분을 지정 
	// adivce : 특정 관점에 실행될 메소드
	
	
	// execution( [접근제한자] 리턴타입 패키지명 클래스명 메소드([매개변수])
	// * : 모든, 아무 값
	// ..  : 하위 모든 패키지 / 0개 이상의 매개변수
	
	// 모든 컨트롤러로 요청이 전달되기 전에 수행
	//@Before("execution(* edu.kh.fin..*Controller.*(..))")
	//@Before("controllerPointcut()")
	// edu.kh.fin 하위에 Controller라는 이름이 들어간 클래스의 모든 메소드
	public void startLine() {
		System.out.println("---------- 요청 처리 시작 ----------");
	}
	
	//@After("execution(* edu.kh.fin..*Controller.*(..))")
	//@After("controllerPointcut()")
	public void endLine() {
		System.out.println("---------- 요청 처리 종료 ----------");
	}
	
	
	// 작성하기 어려운 Pointcut을 변수처럼 사용하게 해주는 어노테이션
	@Pointcut("execution(* edu.kh.fin..*Controller.*(..))")
	public void controllerPointcut() {}
	
	
	@Pointcut("execution(* edu.kh.fin..*ServiceImpl.*(..))")
	public void serviceImplPointcut() {}
	
	
	
	
	
	
	
	
}
