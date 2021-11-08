package edu.kh.fin.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.fin.member.model.dao.MemberDAO;
import edu.kh.fin.member.model.vo.Member;

// Service : 비즈니스 로직( 트랜잭션, 데이터가공 등)
@Service // Service 레이어, 비즈니스 로직을 가진 클래스임을 명시 + Bean 등록
public class MemberServiceImpl implements MemberService {
	
	// 의존성 주입(DI) : 개발자가 객체를 만들지 않고
	//					 스프링 프레임워크가 같은 타입의 Bean을 주입해 주는 것
	
	// 암호화를 위한 객체 의존성 주입(DI)
	@Autowired // 등록된 bean 중에서 같은 타입의 bean 자동으로 연결해줌
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	// MemberDAO 객체 의존성 주입(DI)
	@Autowired
	private MemberDAO dao;
	
	
	// 로그인 Service
	@Override
	public Member login(Member inputMember) {
		
		System.out.println(inputMember); // 아이디, 비밀번호
		System.out.println("변경 전 비밀번호 : " + inputMember.getMemberPw());
		
		// 입력 받은 비밀번호를 암호화
		String encPw = bCryptPasswordEncoder.encode( inputMember.getMemberPw() );
		System.out.println("변경 후 비밀번호 : " + encPw);
		
		
		// DB에서 입력된 정보와 같은 회원 정보를 조회해서 얻어오기
		// bcrypt 암호화는 같은 문자열이라도 결과가 계속 변하기 때문에
		// DB에서 비교할 수 없다!
		// -> 아이디가 일치하는 회원정보(암호화된 비밀번호 포함)를 모두 얻어와 
		// bcrypt에서 제공하는 메소드를 이용해 비밀번호를 비교해야 한다!
		Member loginMember = dao.login(inputMember.getMemberId());
		
		// DB 조회 결과 확인
		System.out.println(loginMember);
		
		// 1) 아이디 일치하는 회원이 있었는지 검사
		if(loginMember != null) {
			
			// 2) (중요!) 입력 받은 비밀번호와 DB 저장 암호화 비밀번호를 비교하여 일치하지 않는 경우
			if( !bCryptPasswordEncoder.matches(inputMember.getMemberPw(), loginMember.getMemberPw()) ) {
											// 입력 받은 비밀번호     ,   DB 저장된 암호화 비밀번호
				loginMember = null;
				// 조회 해온 회원 정보를 없앰.
			
			}else {
				// 3) 비밀번호가 일치하면
				loginMember.setMemberPw(null); // 비밀번호만 없앰.
			}
		}
		
		return loginMember;
	}


	// 아이디 중복 검사 Service
	@Override
	public int idDupCheck(String id) {
		return dao.idDupCheck(id);
	}


	/* 스프링에서 트랜잭션 처리를 하는 방법
	 * 1. 코드 기반 처리 방법 -> 기존 방법
	 * 2. 선언적 트랜잭션 처리 방법
	 * 	1) <tx:advice> XML 작성 방법
	 *  2) @Transactional 어노테이션 작성 방법
	 *  * 트랜잭션 처리를 위한 매니저가 bean으로 등록되어 있어야 함.
	 * 
	 * @Transactional이 commit/rollback을 하는 기준
	 * -> 해당 메소드 내에서 RuntimeException이 발생하면 rollback, 발생하지 않으면 commit
	 * -> 발생하는 예외의 기준을 바꾸는 방법 : rollbackFor 속성을 사용.
	 * */
	
	// 회원 가입 Service
	@Transactional (rollbackFor = Exception.class) // 어떤 예외든 발생하면 rollback
	@Override
	public int signUp(Member inputMember) {
		
		// ** 비밀번호를 bcrypt 암호화를 이용해서 변경하는 작업
		String encPwd = bCryptPasswordEncoder.encode( inputMember.getMemberPw() );
		
		// 암호화된 비밀번호를 inputMember에 세팅
		inputMember.setMemberPw(encPwd);
		
		// DB에 inputMember에 담긴 내용을 삽입 후 결과를 다시 Controller로 돌려보냄
		return dao.signUp(inputMember);
	}


	
	// 회원 정보 수정 Service
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateMember(Member inputMember) {
		return dao.updateMember(inputMember);
	}


	// 회원 비밀번호 변경 Service
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int changePwd(String currentPwd, String newPwd, Member loginMember) {
		
		// 1) 암호화를 먼저 고려해야한다
		// - 현재 비밀번호가 일치하는지 확인 먼저 진행해야 한다!
		//  --> 왜? bcrypt 암호화를 사용하고 있기 때문에
		//   -- 왜죠? bcrypt 암호화는 비밀번호를 조회해온 후 matches() 메소드로 비교해야함
		
		// DB에 저장되어 있는 현재 회원의 비밀번호 조회
		String savePwd = dao.selectPassword(loginMember.getMemberNo());
		
		int result = 0; // 결과 저장용 변수
		
		// 조회한 비밀번호와 입력 받은 현재 비밀번호가 일치하는지 확인
		if( bCryptPasswordEncoder.matches(currentPwd, savePwd)  ) {
			
			// 2) 비밀번호 변경
			// - 새 비밀번호를 암호화
			String encPwd = bCryptPasswordEncoder.encode(newPwd);
			
			// 마이바티스 메소드는 SQL 수행 시 사용할 파라미터를
			// 하나만 추가할 수 있다! -> loginMember에 담아서 전달
			loginMember.setMemberPw(encPwd);
			
			result = dao.changePwd(loginMember);
			
			// loginMember에 저장한 encPwd를 제거 (Session에 비밀번호 저장하면 안된다!)
			loginMember.setMemberPw(null);
		}
		
		return result;
	}


	// 회원 탈퇴 Service
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int secession(String currentPwd, int memberNo) {
		
		// 저장된 비밀번호를 얻어와 비교
		String savePwd = dao.selectPassword(memberNo);
		
		int result = 0;
		if( bCryptPasswordEncoder.matches(currentPwd, savePwd)  ) {
			
			result = dao.secession(memberNo);
			
		}
		
		return result;
	}
	
	
	
	
	
	
	
	
	

}
