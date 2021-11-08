package edu.kh.fin.chat.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.fin.chat.model.dao.ChatDAO;
import edu.kh.fin.chat.model.vo.ChatMessage;
import edu.kh.fin.chat.model.vo.ChatRoom;
import edu.kh.fin.chat.model.vo.ChatRoomJoin;

@Service
public class ChatServiceImpl implements ChatService{
	
	@Autowired
	private ChatDAO dao;

	// 채팅방 목록 조회 Service
	@Override
	public List<ChatRoom> selectRoomList() {
		return dao.selectRoomList();
	}

	// 채팅방 만들기 Service
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int openChatRoom(ChatRoom room) {
		
		// 바로 insert 진행하면 만들어진 채팅방 번호를 알 수 없다!
		// -> 다음 채팅방 번호를 얻어와 해당 번호로 insert하면 된다.
		int chatRoomNo = dao.nextChatRoomNo();
		
		if(chatRoomNo > 0) { // 다음 번호 조회 성공 시
			
			room.setChatRoomNo(chatRoomNo); // room에다가 조회한 번호 세팅
											// 마이바티스 메소드는 파라미터를 하나만 전달할 수 있다
											// 여러개 전달할 경우 묶어서 전달해야 한다.
			
			
			int result = dao.openChatRoom(room);
			
			if(result == 0) { // 실패한 경우
				chatRoomNo = 0;
			}
			
		}
		
		return chatRoomNo;
	}

	
	// 채팅방 입장 + 내용 얻어오기 Service
	@Override
	public List<ChatMessage> joinChatRoom(ChatRoomJoin join) {
		
		try {
			dao.joinChatRoom(join); // 중복 입장 시 100% 예외가 발생함
		}catch (Exception e) {	}
		// 예외 발생 시 catch로 잡아서 예외를 없애버림.
		
		return dao.selectChatMessage(join.getChatRoomNo()); // 채팅방 내용 얻어오기
	}

	
	// 채팅 내용 삽입
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int insertMessage(ChatMessage cm) {
		return dao.insertMessage(cm);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
}
