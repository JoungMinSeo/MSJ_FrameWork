package edu.kh.fin.board.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.fin.board.model.vo.Reply;

@Repository
public class ReplyDAO {

	@Autowired
	private SqlSessionTemplate sqlSession;

	
	/** 댓글 목록 조회
	 * @param boardNo
	 * @return rList
	 */
	public List<Reply> selectList(int boardNo) {
		return sqlSession.selectList("replyMapper.selectList", boardNo);
	}


	/** 댓글 삽입
	 * @param reply
	 * @return result
	 */
	public int insertReply(Reply reply) {
		return sqlSession.insert("replyMapper.insertReply", reply);
		// SEQ_RNO
	}


	/** 댓글 수정
	 * @param reply
	 * @return result
	 */
	public int updateReply(Reply reply) {
		return sqlSession.update("replyMapper.updateReply", reply);
	}


	/** 댓글 삭제
	 * @param replyNo
	 * @return result
	 */
	public int deleteReply(int replyNo) {
		return sqlSession.update("replyMapper.deleteReply", replyNo);
	}
	
	
	
	
	
	
}
