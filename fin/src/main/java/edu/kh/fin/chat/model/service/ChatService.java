package edu.kh.fin.chat.model.service;

import java.util.List;

import edu.kh.fin.chat.model.vo.ChatMessage;
import edu.kh.fin.chat.model.vo.ChatRoom;
import edu.kh.fin.chat.model.vo.ChatRoomJoin;

public interface ChatService {

	
	/** 채팅방 목록 조회 Service
	 * @return
	 */
	List<ChatRoom> selectRoomList();

	/** 채팅방 만들기 Service
	 * @param room
	 * @return chatRoomNo
	 */
	int openChatRoom(ChatRoom room);

	
	/** 채팅방 입장 + 내용 얻어오기 
	 * @param join
	 * @return list
	 */
	List<ChatMessage> joinChatRoom(ChatRoomJoin join);

	/** 채팅 내용 삽입
	 * @param cm
	 * @return result
	 */
	int insertMessage(ChatMessage cm);

	
	
	
	
	
	
}


