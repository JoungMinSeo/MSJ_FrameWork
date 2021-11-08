package edu.kh.fin.member.model.service;

import edu.kh.fin.member.model.vo.Member;

public interface MemberService {

	/* Service interface를 만드는 이유
	 * 
	 * 1. 프로젝트의 규칙성을 부여하기 위해서
	 * 	-> 인터페이스를 상속 받으면 동일한 형태의 메소드가 강제됨.
	 * 
	 * 2. 클래스간의 결합도를 낮추고, 유지보수성 향상을 위해서
	 * 
	 * 3. Spring의 AOP사용하기 위함
	 *  -> AOP는 spring-proxy를 이용해서 동작하는데
	 *   spring-proxy는 Service 인터페이스를 상속받아 동작함
	 * 
	 * */
	
	// 인터페이스에서 메소드는 모두 묵시적으로 public abstract 이다.
	public abstract Member login(Member inputMember);

	
	/** 아이디 중복 검사 Service
	 * @param id
	 * @return result
	 */
	public abstract int idDupCheck(String id);


	/** 회원 가입 Service
	 * @param inputMember
	 * @return result
	 */
	public abstract int signUp(Member inputMember);


	/** 회원 정보 수정 Service
	 * @param inputMember
	 * @return result
	 */
	public abstract int updateMember(Member inputMember);


	/** 회원 비밀번호 변경 Service
	 * @param currentPwd
	 * @param newPwd 
	 * @param loginMember
	 * @return result
	 */
	public abstract int changePwd(String currentPwd, String newPwd, Member loginMember);


	/** 회원 탈퇴 Service
	 * @param currentPwd
	 * @param memberNo
	 * @return result
	 */
	public abstract int secession(String currentPwd, int memberNo);
	
	
	
	
	
	
}
