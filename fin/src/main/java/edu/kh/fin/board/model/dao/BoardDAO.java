package edu.kh.fin.board.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.fin.board.model.vo.Attachment;
import edu.kh.fin.board.model.vo.Board;
import edu.kh.fin.board.model.vo.Category;
import edu.kh.fin.board.model.vo.Pagination;
import edu.kh.fin.board.model.vo.Search;

@Repository
public class BoardDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	/** 특정 게시판 전체 게시글 수 조회
	 * @param boardType
	 * @return pagination
	 */
	public Pagination getListCount(int boardType) {
		return sqlSession.selectOne("boardMapper.getListCount", boardType);
	}
	
	/** 특정 게시판 검색 게시글 수 조회
	 * @param search
	 * @return pagination
	 */
	public Pagination getSearchListCount(Search search) {
		return sqlSession.selectOne("boardMapper.getSearchListCount", search);
	}
	
	

	/** 게시글 목록 조회
	 * @param pagination
	 * @return boardList
	 */
	public List<Board> selectBoardList(Pagination pagination) {
		
		/* 마이바티스의 RowBounds 객체
		 * -> 조회결과 ResultSet에서 어디서 부터 어디까지만 딱 잘라서 얻어올 수 있게하는 객체
		 * 
		 * offset : 조회결과를 몇 행을 건너 뛸 것인지 지정
		 * */
		
		int offset = (pagination.getCurrentPage() - 1) * pagination.getLimit();
		
		RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());
		// offset 만큼 건너 뛰고, limit만큼의 행을 얻어옴
		
		return sqlSession.selectList("boardMapper.selectBoardList", pagination.getBoardType(), rowBounds);
	}
	
	
	/** 게시글 목록 조회(검색)
	 * @param search
	 * @param pagination
	 * @return boardList
	 */
	public List<Board> selectSearchList(Search search, Pagination pagination) {
		// RowBounds : 지정된 offset 만큼 건너 뛴 후 몇 행을 조회할지 지정하는 객체
		int offset = (pagination.getCurrentPage() - 1) * pagination.getLimit();
		
		RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());
		
		return sqlSession.selectList("boardMapper.selectSearchList", search, rowBounds);
	}

	
	

	
	 
	/** 게시글 상세 조회
	 * @param boardNo
	 * @return board
	 */
	public Board selectBoard(int boardNo) {
		return sqlSession.selectOne("boardMapper.selectBoard",boardNo);
	}

	
	/** 조회수 증가
	 * @param boardNo
	 * @return result
	 */
	public int increaseReadCount(int boardNo) {
		return sqlSession.update("boardMapper.increaseReadCount", boardNo);
	}

	/** 카테고리 목록 조회
	 * @return category
	 */
	public List<Category> selectCategory() {
		return sqlSession.selectList("boardMapper.selectCategory");
	}

	/** 게시글 삽입
	 * @param board
	 * @return boardNo
	 */
	public int insertBoard(Board board) {
		int result = sqlSession.insert("boardMapper.insertBoard", board);
		// insert 성공 시 1, 실패 시 0
		
		// mapper 에서 <selectKey> 수행 결과인 게시글 번호를
		// 얕은 복사로 전달한 board에 추가했음.
		
		if(result > 0) {
			return board.getBoardNo();
		}else {
			return 0;
		}
		
	}

	
	/** 파일 정보 삽입(List)
	 * @param atList
	 * @return result
	 */
	public int insertAttachmentList(List<Attachment> atList) {
		return sqlSession.insert("boardMapper.insertAttachmentList", atList);
	}

	/** 게시글 수정
	 * @param board
	 * @return result
	 */
	public int updateBoard(Board board) {
		return sqlSession.update("boardMapper.updateBoard", board);
	}

	
	/** 첨부 파일 정보 삭제
	 * @param map
	 */
	public void deleteAttachment(Map<String, Object> map) {
		sqlSession.delete("boardMapper.deleteAttachment", map);
	}

	/** 첨부 파일 정보 수정(한 행)
	 * @param at
	 * @return result
	 */
	public int updateAttachment(Attachment at) {
		return sqlSession.update("boardMapper.updateAttachment", at);
	}

	/** 첨부 파일 정보 삽입(한 행)
	 * @param at
	 * @return result
	 */
	public int insertAttachment(Attachment at) {
		return sqlSession.insert("boardMapper.insertAttachment", at);
	}

	
	
	/** 72시간 보다 더 과거에 추가된 파일명 조회
	 * @param standard
	 * @return dbList
	 */
	public List<String> selectDBList(String standard) {
		return sqlSession.selectList("boardMapper.selectDBList",standard);
	}


	
	
	
	
}
