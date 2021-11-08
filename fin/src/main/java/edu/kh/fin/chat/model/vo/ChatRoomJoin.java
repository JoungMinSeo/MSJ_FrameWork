package edu.kh.fin.chat.model.vo;

public class ChatRoomJoin {
	private int memberNo;
	private int chatRoomNo;
	
	public ChatRoomJoin() {
		// TODO Auto-generated constructor stub
	}

	public int getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(int memberNo) {
		this.memberNo = memberNo;
	}

	public int getChatRoomNo() {
		return chatRoomNo;
	}

	public void setChatRoomNo(int chatRoomNo) {
		this.chatRoomNo = chatRoomNo;
	}

	@Override
	public String toString() {
		return "ChatRoomJoin [memberNo=" + memberNo + ", chatRoomNo=" + chatRoomNo + "]";
	}
	
}
