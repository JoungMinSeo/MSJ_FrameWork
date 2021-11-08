package edu.kh.fin.chat.model.vo;

public class ChatRoom {
	private int chatRoomNo;
	private String title;
	private String status;
	private int memberNo;
	private String memberName;
	private int cnt; // 참여자 수
	
	public ChatRoom() {
		// TODO Auto-generated constructor stub
	}

	public int getChatRoomNo() {
		return chatRoomNo;
	}

	public void setChatRoomNo(int chatRoomNo) {
		this.chatRoomNo = chatRoomNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	@Override
	public String toString() {
		return "ChatRoom [chatRoomNo=" + chatRoomNo + ", title=" + title + ", status=" + status + ", memberNo="
				+ memberNo + ", memberName=" + memberName + ", cnt=" + cnt + "]";
	}

	
	
}
