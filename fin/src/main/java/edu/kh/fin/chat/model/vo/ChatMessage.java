package edu.kh.fin.chat.model.vo;

import java.sql.Timestamp;

public class ChatMessage {
	private int cmNo;
	private String message;
	private Timestamp createDate;
	private int chatRoomNo;
	private int memberNo;
	private String memberName;
	
	public ChatMessage() {
		// TODO Auto-generated constructor stub
	}

	public int getCmNo() {
		return cmNo;
	}

	public void setCmNo(int cmNo) {
		this.cmNo = cmNo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public int getChatRoomNo() {
		return chatRoomNo;
	}

	public void setChatRoomNo(int chatRoomNo) {
		this.chatRoomNo = chatRoomNo;
	}

	public int getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(int memberNo) {
		this.memberNo = memberNo;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	@Override
	public String toString() {
		return "ChatMessage [cmNo=" + cmNo + ", message=" + message + ", createDate=" + createDate + ", chatRoomNo="
				+ chatRoomNo + ", memberNo=" + memberNo + ", memberName=" + memberName + "]";
	}
	
	
}
