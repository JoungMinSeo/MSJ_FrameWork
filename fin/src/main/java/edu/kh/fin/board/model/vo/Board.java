package edu.kh.fin.board.model.vo;

import java.sql.Timestamp;
import java.util.List;

public class Board {

	private int boardNo;
	private String boardTitle;
	private String memberName;
	private String categoryName;
	private int readCount;	
	private Timestamp createDate;
	
	private String boardContent;	// 글 내용
	private int memberNo;			// 작성 회원 번호
	private Timestamp modifyDate;	// 마지막 수정일
	private List<Attachment> atList;// 게시글에 첨부된 파일(이미지) 목록
	
	int boardType;
	
	// 삽입 시 필요한 필드 추가
	private int categoryCode;
	
	
	public Board() {
		// TODO Auto-generated constructor stub
	}


	public int getBoardNo() {
		return boardNo;
	}


	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}


	public String getBoardTitle() {
		return boardTitle;
	}


	public void setBoardTitle(String boardTitle) {
		this.boardTitle = boardTitle;
	}


	public String getMemberName() {
		return memberName;
	}


	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}


	public String getCategoryName() {
		return categoryName;
	}


	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}


	public int getReadCount() {
		return readCount;
	}


	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}


	public Timestamp getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}



	public String getBoardContent() {
		return boardContent;
	}


	public void setBoardContent(String boardContent) {
		this.boardContent = boardContent;
	}


	public int getMemberNo() {
		return memberNo;
	}


	public void setMemberNo(int memberNo) {
		this.memberNo = memberNo;
	}


	public Timestamp getModifyDate() {
		return modifyDate;
	}


	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}


	public List<Attachment> getAtList() {
		return atList;
	}


	public void setAtList(List<Attachment> atList) {
		this.atList = atList;
	}


	public int getCategoryCode() {
		return categoryCode;
	}


	public void setCategoryCode(int categoryCode) {
		this.categoryCode = categoryCode;
	}


	public int getBoardType() {
		return boardType;
	}


	public void setBoardType(int boardType) {
		this.boardType = boardType;
	}


	@Override
	public String toString() {
		return "Board [boardNo=" + boardNo + ", boardTitle=" + boardTitle + ", memberName=" + memberName
				+ ", categoryName=" + categoryName + ", readCount=" + readCount + ", createDate=" + createDate
				+ ", boardContent=" + boardContent + ", memberNo=" + memberNo + ", modifyDate=" + modifyDate
				+ ", atList=" + atList + ", boardType=" + boardType + ", categoryCode=" + categoryCode + "]";
	}


	
	

}
