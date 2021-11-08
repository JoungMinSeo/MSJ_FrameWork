package edu.kh.fin.board.model.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.fin.board.exception.InsertAttachmentException;
import edu.kh.fin.board.exception.SaveFileException;
import edu.kh.fin.board.model.dao.BoardDAO;
import edu.kh.fin.board.model.vo.Attachment;
import edu.kh.fin.board.model.vo.Board;
import edu.kh.fin.board.model.vo.Category;
import edu.kh.fin.board.model.vo.Pagination;
import edu.kh.fin.board.model.vo.Search;

@Service
public class BoardServiceImpl implements BoardService{

	@Autowired
	private BoardDAO dao;

	// 전체 게시글 수 + 게시판 이름 조회
	@Override
	public Pagination getPagination(Pagination pg) {
		
		// 1) 전체 게시글 수 조회
		Pagination selectPg = dao.getListCount(pg.getBoardType());
		
		// 2) 계산이 완료된 Pagination 객체 생성 후 반환
		return new Pagination(pg.getCurrentPage(), selectPg.getListCount(),
							pg.getBoardType(), selectPg.getBoardName());
	}

	
	// 전체 게시글 수 + 게시판 이름 조회(검색)
	@Override
	public Pagination getPagination(Search search, Pagination pg) {
		// 1) 검색이 적용된 게시글 수 조회
		Pagination selectPg = dao.getSearchListCount(search);
		
		//System.out.println(selectPg);
		
		// 2) 계산이 완료된 Pagination 객체 생성 후 반환
		return new Pagination(pg.getCurrentPage(), selectPg.getListCount(),
				pg.getBoardType(), selectPg.getBoardName());
	}



	// 게시글 목록 조회
	@Override
	public List<Board> selectBoardList(Pagination pagination) {
		return dao.selectBoardList(pagination);
	}
	
	
	// 게시글 목록 조회(검색)
	@Override
	public List<Board> selectBoardList(Search search, Pagination pagination) {
		return dao.selectSearchList(search, pagination);
	}


	// 게시글 상세 조회
	@Transactional(rollbackFor = Exception.class) // 모든 예외 발생시 롤백
	@Override
	public Board selectBoard(int boardNo) {
		// 1) 게시글 상세 조회
		Board board =  dao.selectBoard(boardNo);
		
		// 2) 게시글 상세 조회 성공 시 조회수 1 증가
		if(board != null) {
			dao.increaseReadCount(boardNo);
			
			// 3) 조회된 board의 readCount와 DB의 READ_COUNT 동기화
			board.setReadCount(  board.getReadCount() + 1  );
		}
		
		
		return board;
	}

	// 카테고리 목록 조회
	@Override
	public List<Category> selectCategory() {
		return dao.selectCategory();
	}


	// 게시글 삽입
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int insertBoard(Board board, List<MultipartFile> images, String webPath, String savePath) {
		
		// 1) 크로스사이트 스크립트 방지 처리 + 개행 문자 처리
		board.setBoardTitle( replaceParameter(  board.getBoardTitle()  )  );
		board.setBoardContent( replaceParameter(  board.getBoardContent()  )  );
		
		board.setBoardContent(  board.getBoardContent().replaceAll("(\r\n|\r|\n|\n\r)", "<br>")  );
		
		// 2) 글 부분 삽입
		// 기존) 다음 글 번호를 조회한 후 게시글을 삽입
		// 마이바티스 사용) insert 후 특정 컬럼 값을 반환하게 만드는 useGeneratedKeys, <selectKey> 사용
		
		int boardNo = dao.insertBoard(board);
		
		//System.out.println("boardNo : " + boardNo);
		
		if(boardNo > 0) { // 게시글 삽입 성공
			
			// 3) 파일 정보 DB 삽입
			
			// images에는 파일이 업로드가 됐든, 말든
			// input type="file" 태그가 모두 담겨 있다
			// -> 구별하는 방법 : 파일이 업로드되었을 경우 파일명이 존재한다.
			
			// DB에 저장할 파일만 추가할 List
			List<Attachment> atList = new ArrayList<Attachment>(); 
			
			for(int i=0 ; i<images.size() ; i++) {
				
				if( !images.get(i).getOriginalFilename().equals("")  ) { // 파일이 업로드 된 경우
					// images의 i번째 요소의 파일명이 ""이 아닐 경우
					// -> 업로드된 파일이 없을 경우 파일명이 ""(빈문자열)로 존재함
					
					// 파일명 변경 작업 수행
					String fileName = rename(images.get(i).getOriginalFilename());
					
					// Attachment 객체 생성
					Attachment at = new Attachment();
					at.setFileName(fileName); // 변경한 파일명
					at.setFilePath(webPath); // 웹 접근 경로
					at.setBoardNo(boardNo); // 게시글 삽입 결과(게시글 번호)
					at.setFileLevel(i); // for문 반복자 == 파일레벨
					
					// 만들어진 객체를 atList에 추가
					atList.add(at);
				}
			}
			
			
			// 업로드된 이미지가 있을 경우에만 DAO 호출
			if(!atList.isEmpty()) { // atList가 비어있지 않을 때
				
				int result = dao.insertAttachmentList(atList);
				// result == 성공한 행의 개수
				
				// 삽입이 제대로 되었는지 확인 어떻게?
				// atList의 크기 == result
				if(atList.size() == result) { // 모두 삽입 성공한 경우
					
					// 4) 파일을 서버에 저장(transfer() )
					for(int i=0 ; i<atList.size() ; i++) {
						try {
							images.get( atList.get(i).getFileLevel() )
							.transferTo(new File(savePath + "/" + atList.get(i).getFileName()  ));
							// images에서 업로된 파일이 있는 요소를 얻어와
							// 지정된 경로에 파일로 저장
							
							
						} catch (Exception e) {
							e.printStackTrace();
							
							// tranrferTo() 메소드는 반드시 예외처리를 해야되는
							// IOException을 발생시킬 가능성이 있는 메소드다.
							
							// 그런데, 예외가 발생하면 rollback을 수행하는 @Transactional이 작성되어 있는데
							// catch로 예외를 처리하면 이를 인지 못하는 상황이 발생함.
							// 그런다고 해서 catch문을 없애면 예외처리하라고 에러가 발생함.
							
							// -> 예외는 발생 시키지만 코드상에서 예외처리가 강제되지 않는
							//    UnChecked Exception 구문을 만들어서 예외를 발생시킴.
							
							// throw new NullPointerException(); // -> 사용은 가능하지만 적절하지 않음.
							// -> 사용자 정의 예외 클래스를 생성해서 사용!
							
							throw new SaveFileException();
							// 예외가 던저짐 -> @Transactional이 반응함 -> 롤백이 수행됨.
							
						}
						
					}
					
					
				}else { // 삽입이 하나라도 실패한 경우
					throw new SaveFileException();
					
					// ex) 4개의 파일 정보를 DB에 삽입 했으나 2개만 성공한 경우
				}
			}
		}
		
		return boardNo;
	}
	

	// 게시글 수정용 상세 조회
	@Override
	public Board selectUpdateBoard(int boardNo) {
		Board board = dao.selectBoard(boardNo);
		
		// <br> -> \r\n으로 변경
		board.setBoardContent( board.getBoardContent().replaceAll("<br>", "\r\n") );
		
		return board;
	}

	
	// 게시글 수정
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateBoard(Board board, List<MultipartFile> images, String webPath, String savePath, String deleteImages) {
		
		// 1) 크로스사이트 스크립트 방지 처리 + 개행문자 처리(\r\n -> <br>)
		board.setBoardTitle( replaceParameter(  board.getBoardTitle()  )  );
		board.setBoardContent( replaceParameter(  board.getBoardContent()  )  );
		
		board.setBoardContent(  board.getBoardContent().replaceAll("(\r\n|\r|\n|\n\r)", "<br>")  );
		
		// 2) 글 부분만 수정
		int result = dao.updateBoard(board);
		
		// 3) 이미지 관련 코드 작성
		if(result > 0 ) {
			// 3-1) deleteImages와 일치하는 파일레벨의 ATTACHMENT 행 삭제
			// deleteImage : 삭제해야할 이미지 파일 레벨을 ","를 구분자로 하여 만들어진 String
			
			if( !deleteImages.equals("") ) { // 삭제할 파일 레벨이 존재하는 경우
				// DB 삭제 구문에 필요한 값 : deleteImages, boardNo
				// 두 데이터를 한번에 담을 VO가 없음 -> Map 사용
				
				Map<String , Object> map = new HashMap<String, Object>();
				map.put("boardNo", board.getBoardNo());
				map.put("deleteImages", deleteImages);
				
				// 반환값이 아무런 의미를 갖지 못하므로 반환 받을 필요가 없다.
				dao.deleteAttachment(map);
			}
			
			// 3-2) 수정된 이미지 정보 update
			List<Attachment> atList = new ArrayList<Attachment>(); 
			
			for(int i=0 ; i<images.size() ; i++) {
				
				if( !images.get(i).getOriginalFilename().equals("")  ) { // 파일이 업로드 된 경우
					// images의 i번째 요소의 파일명이 ""이 아닐 경우
					// -> 업로드된 파일이 없을 경우 파일명이 ""(빈문자열)로 존재함
					
					// 파일명 변경 작업 수행
					String fileName = rename(images.get(i).getOriginalFilename());
					
					// Attachment 객체 생성
					Attachment at = new Attachment();
					at.setFileName(fileName); // 변경한 파일명
					at.setFilePath(webPath); // 웹 접근 경로
					at.setBoardNo(board.getBoardNo()); // 수정중인 게시글 번호
					at.setFileLevel(i); // for문 반복자 == 파일레벨
					
					// 만들어진 객체를 atList에 추가
					atList.add(at);
				}
			}
			
			System.out.println(atList);
			
			// atList를 하나씩 반복 접근하여 한 행씩 update 진행
			for(Attachment at : atList) {
				result = dao.updateAttachment(at);
				System.out.println("update result : " + result);
				
				// update를 시도했으나 결과가 0이 나온경우
				// == 기존에 이미지가 없던 레벨에 새로운 이미지가 추가되었다
				// -> insert 진행
				
				// 3-3) 기존에 이미지가 없던 레벨을 insert
				if(result == 0) {
					result = dao.insertAttachment(at);
					
					if(result == 0) { // 삽입 실패
						// 강제로 예외를 발생시켜 전체 롤백 수행
						throw new InsertAttachmentException();
					}
				}
			}
			
			
			// 4) 새로 업로드된 이미지 서버에 저장
			for(int i=0 ; i<atList.size() ; i++) {
				try {
					images.get( atList.get(i).getFileLevel() )
					.transferTo(new File(savePath + "/" + atList.get(i).getFileName()  ));
				} catch (Exception e) {
					e.printStackTrace();
					throw new SaveFileException();
				}
			}
		}
		
		return result;
	}

	// 72시간 보다 더 과거에 추가된 파일명 조회
	@Override
	public List<String> selectDBList(String standard) {
		return dao.selectDBList(standard);
	}

	
	

	// 크로스 사이트 스크립트 방지 처리 메소드
	public static String replaceParameter(String param) {
		String result = param;
		if(param != null) {
			result = result.replaceAll("&", "&amp;");
			result = result.replaceAll("<", "&lt;");
			result = result.replaceAll(">", "&gt;");
			result = result.replaceAll("\"", "&quot;");
		}
		
		return result;
	}
	
	
	// 파일명 변경 메소드
	private String rename(String originFileName) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = sdf.format(new java.util.Date(System.currentTimeMillis()));
		
		int ranNum = (int)(Math.random()*100000); // 5자리 랜덤 숫자 생성
		
		String str = "_" + String.format("%05d", ranNum);
		
		String ext = originFileName.substring(originFileName.lastIndexOf("."));
		
		return date + str + ext;
	}
	
	
	
	
	
}
