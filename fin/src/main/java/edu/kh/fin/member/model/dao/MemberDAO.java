package edu.kh.fin.member.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.fin.member.model.vo.Member;

/**
 * @author user1
 *
 */
@Repository // 퍼시스턴스(Persistence, 영속성) 레이어, 영속성을 가지는 속성(파일, DB)와 관련된 클래스 + bean 등록
public class MemberDAO {
	
	// root-context.xml에서 등록한 SqlSessionTemplate bean을 의존성 주입.
	@Autowired
	private SqlSessionTemplate sqlSession; // Connection + 마이바티스(stmt, pstmt, rs를 간단히 씀)

	
	/** 로그인 DAO
	 * @param memberId
	 * @return loginMember
	 */
	public Member login(String memberId) {
		return sqlSession.selectOne("memberMapper.login", memberId);
		// selectOne() : 한 줄 조회 메소드
		// "memberMapper.login" : namespace가 "memberMapper" 인 mapper.xml 파일에서
		//						  id가 login인 태그를 수행
		// memberId : SQL을 수행할 때 사용할 값
	}


	/** 아이디 중복 검사 DAO
	 * @param id
	 * @return result
	 */
	public int idDupCheck(String id) {
		return sqlSession.selectOne("memberMapper.idDupCheck", id);
	}


	/** 회원 가입 DAO
	 * @param inputMember
	 * @return result
	 */
	public int signUp(Member inputMember) {
		return sqlSession.insert("memberMapper.signUp", inputMember  );
	}


	/** 회원 정보 수정 DAO
	 * @param inputMember
	 * @return result
	 */
	public int updateMember(Member inputMember) {
		return sqlSession.update("memberMapper.updateMember", inputMember);
	}


	/** 회원 비밀번호 조회 DAO
	 * @param memberNo
	 * @return savePwd
	 */
	public String selectPassword(int memberNo) {
		return sqlSession.selectOne("memberMapper.selectPassword", memberNo);
	}


	/** 회원 비밀번호 변경 DAO
	 * @param loginMember
	 * @return result
	 */
	public int changePwd(Member loginMember) {
		return sqlSession.update("memberMapper.changePwd", loginMember);
	}


	/** 회원 탈퇴 DAO
	 * @param memberNo
	 * @return result
	 */
	public int secession(int memberNo) {
		return sqlSession.update("memberMapper.secession", memberNo);
	}

	
	
	
	
}
