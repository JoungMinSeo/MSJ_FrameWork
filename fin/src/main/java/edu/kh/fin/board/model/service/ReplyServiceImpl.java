package edu.kh.fin.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.fin.board.model.dao.ReplyDAO;
import edu.kh.fin.board.model.vo.Reply;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyDAO dao;

	// 댓글 목록 조회
	@Override
	public List<Reply> selectList(int boardNo) {
		return dao.selectList(boardNo);
	}

	// 댓글 삽입
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int insertReply(Reply reply) {
		
		// 크로스사이트 스트립트 방지 처리
		reply.setReplyContent(  BoardServiceImpl.replaceParameter( reply.getReplyContent() )   );
		
		// 개행문자 처리
		reply.setReplyContent(  reply.getReplyContent().replaceAll("(\r\n|\r|\n|\n\r)", "<br>")    );
		
		return dao.insertReply(reply);
	}

	// 댓글 수정
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateReply(Reply reply) {
		// 크로스사이트 스트립트 방지 처리
		reply.setReplyContent(  BoardServiceImpl.replaceParameter( reply.getReplyContent() )   );
		
		// 개행문자 처리
		reply.setReplyContent(  reply.getReplyContent().replaceAll("(\r\n|\r|\n|\n\r)", "<br>")    );
		
		return dao.updateReply(reply);
	}

	
	// 댓글 삭제
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int deleteReply(int replyNo) {
		return dao.deleteReply(replyNo);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
