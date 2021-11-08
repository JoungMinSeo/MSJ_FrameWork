package edu.kh.fin.board.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.fin.board.model.vo.Board;
import edu.kh.fin.board.model.vo.Category;
import edu.kh.fin.board.model.vo.Pagination;
import edu.kh.fin.board.model.vo.Search;

public interface BoardService {

	/** 전체 게시글 수 + 게시판 이름 조회
	 * @param pg
	 * @return pagination
	 */
	Pagination getPagination(Pagination pg);
	
	
	/** 전체 게시글 수 + 게시판 이름 조회(검색)
	 * @param search
	 * @param pg
	 * @return pagination
	 */
	Pagination getPagination(Search search, Pagination pg);
	
	

	/** 게시글 목록 조회
	 * @param pagination
	 * @return boardList
	 */
	List<Board> selectBoardList(Pagination pagination);

	
	/** 게시글 목록 조회(검색)
	 * @param search
	 * @param pagination
	 * @return boardList
	 */
	List<Board> selectBoardList(Search search, Pagination pagination);
	
	
	
	/** 게시글 상세 조회
	 * @param boardNo
	 * @return board
	 */
	Board selectBoard(int boardNo);

	/** 카테고리 목록 조회
	 * @return category
	 */
	List<Category> selectCategory();

	/** 게시글 삽입
	 * @param board
	 * @param images
	 * @param webPath
	 * @param savePath
	 * @return boardNo
	 */
	int insertBoard(Board board, List<MultipartFile> images, String webPath, String savePath);

	/** 게시글 수정을 위한 상세조회
	 * @param boardNo
	 * @return board
	 */
	Board selectUpdateBoard(int boardNo);

	/** 게시글 수정
	 * @param board
	 * @param images
	 * @param webPath
	 * @param savePath
	 * @param deleteImages
	 * @return result
	 */
	int updateBoard(Board board, List<MultipartFile> images, String webPath, String savePath, String deleteImages);


	/** 72시간 보다 더 과거에 추가된 파일명 조회
	 * @param standard
	 * @return dbList
	 */
	List<String> selectDBList(String standard);





	
	
	

}
