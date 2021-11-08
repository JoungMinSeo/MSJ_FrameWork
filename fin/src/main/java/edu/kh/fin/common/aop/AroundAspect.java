package edu.kh.fin.common.aop;


import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import edu.kh.fin.member.model.vo.Member;

@Component
@Aspect
@Order(1) // advice 실행 순서 지정. 값이 클수록 먼저 시작, 어노테이션이 없으면 가장 마지막
			// @Around advice의 값이 가장 큰 경우 가장 먼저/ 가장 마지막에 실행됨. 
public class AroundAspect {
	
	// Logger : 로그를 작성할 수 있게하는 객체
	private Logger logger = LoggerFactory.getLogger(AroundAspect.class);
	
	// 전처리, 후처리를 모두 해결하고자 할 때 사용 하는 어드바이스
	// proceed() 메소드 호출 전  : @Before advice 작성
	// proceed() 메소드 호출 후  : @After advice 작성
	// 메소드 마지막에 proceed()의 반환값을 리턴해야함. 
	@Around("TestAspect.serviceImplPointcut()") // @Before + @After
	public Object aroundLogs(ProceedingJoinPoint pp) throws Throwable {
		// @Around advice는 JoinPoint Interface가 아닌
		//  하위 타입인 ProceedingJoinPoint를 사용해야 함.
		// ProceedingJoinPoint : 현재 관점에서 취급할 수 있는 정보가 담겨있음
		
		Member loginMember = null;
		String ip = null;
		
		// 접속자 IP 얻어오기
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			ip = request.getRemoteAddr();
			loginMember = (Member)request.getSession().getAttribute("loginMember");
		}catch (Exception e) {
			logger.info("스케쥴러 동작");
		}
		
		// 클래스명
		String className = pp.getTarget().getClass().getSimpleName(); // 대상 객체의 간단한 클래스명(패키지명 제외)
		
		// 메소드명
		String methodName = pp.getSignature().getName(); // 대상 객체 메소드의 정보 중 메소드명을 반환. 
		
		logger.debug("--------------------------------------------------------------------------------------------------------------");
		logger.debug("[Start] : " + className + " - " + methodName + "()");
		logger.debug("[parameter] : " + Arrays.toString(pp.getArgs()));
		// jp.getArgs() : 대상 메소드의 매개 변수를 반환한다.
		
		if(ip != null)
			logger.debug("[ip] : " + ip);
		
		
		if(loginMember != null) {
			logger.debug("[id] : " + loginMember.getMemberId());
		}

		
		long startMs = System.currentTimeMillis(); // 서비스 시작 시의 ms 값
		
		Object obj = pp.proceed(); // 여기가 기준
		
		long endMs = System.currentTimeMillis(); // 서비스 종료 시의 ms 값
		
			
		logger.debug("[Running Time] : " + (endMs- startMs) + "ms");
		logger.debug("[End] : " + className + " - " + methodName + "()");
		logger.debug("--------------------------------------------------------------------------------------------------------------");
		// jp.getArgs() : 대상 메소드의 매개 변수를 반환한다.

		
		return obj;
		
	}
}






