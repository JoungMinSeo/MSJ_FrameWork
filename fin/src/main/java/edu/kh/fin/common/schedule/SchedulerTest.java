package edu.kh.fin.common.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component // bean 등록
public class SchedulerTest {
	/*
	 * @Scheduled
	 * 
	 * * Spring에서 제공하는 스케줄러 - 스케줄러 : 시간에 따른 특정 작업(Job)의 순서를 지정하는 방법.
	 * 
	 * 설정 방법 
	 * 1) servlet-context.xml -> Namespaces 탭 -> task 체크 후 저장
	 * 2) servlet-context.xml -> Source 탭 -> <task:annotation-driven/> 추가
	 * 
	 * 
	 */
	 /* @Scheduled 속성
	 *  - fixedDelay : 이전 작업이 끝난 시점으로 부터 고정된 시간(ms)을 설정.
	 *  - fixedRate : 이전 작업이 수행되기 시작한 시점으로 부터 고정된 시간(ms)을 설정.
	 * 
	 * * cron 속성 : UNIX계열 잡 스케쥴러 표현식으로 작성 
	 * - cron="초 분 시 일 월 요일 [년도]" 
	 * - 요일 : 1(SUN) ~ 7(SAT) 
	 * ex) 2019년 9월 16일 월요일 10시 30분 20초 cron="20 30 10 16 9 2" // 연도 생략 가능
	 * 
	 * - 특수문자 
	 * * : 모든 수. 
	 * - : 두 수 사이의 값. ex) 10-15 -> 10이상 15이하 
	 * , : 특정 값 지정. ex) 3,4,7 -> 3,4,7 지정 
	 * / : 값의 증가. ex) 0/5 -> 0부터 시작하여 5마다 
	 * ? : 특별한 값이 없음. (월, 요일만 해당) 
	 * L : 마지막. (월, 요일만 해당)
	 * 
	 * * 주의사항 - @Scheduled 어노테이션은 매개변수가 없는 메소드에만 적용 가능.
	 * 
	 */
	
	//@Scheduled(fixedDelay = 5000) // 메소드 종료 후 5초 후에 다시 메소드 실행, ms 단위
	//@Scheduled(fixedRate = 3000) // 메소드 실행 시점으로 부터 5초 후에 다시 메소드 실행, ms 단위
					//초 분 시 일 월 요일 [년도]
	//@Scheduled(cron = "0 * * * * *") // == 매 분 0초마다 실행
	@Scheduled(cron = "0 0 0 * * *") // == 자정 마다 실행 ex) 하루 누적 방문자 수, 주기 적인 크롤링
	public void test1() {
		System.out.println("자정 마다 실행중...");
	}
	
	
	
	
	
	
	
}
