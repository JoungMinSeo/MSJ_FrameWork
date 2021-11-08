package edu.kh.fin.chat.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.fin.chat.model.vo.ChatMessage;
import edu.kh.fin.chat.model.vo.ChatRoom;
import edu.kh.fin.chat.model.vo.ChatRoomJoin;

@Repository
public class ChatDAO {

	@Autowired
	private SqlSessionTemplate sqlSession;

	/** 채팅방 목록 조회 DAO
	 * @return chatRoomList
	 */
	public List<ChatRoom> selectRoomList() {
		return sqlSession.selectList("chatMapper.selectChatRoomList");
	}

	/** 다음 채팅방 번호 조회 DAO
	 * @return chatRoomNo
	 */
	public int nextChatRoomNo() {
		return sqlSession.selectOne("chatMapper.nextChatRoomNo");
	}

	/** 채팅방 만들기 DAO
	 * @param room
	 * @return result
	 */
	public int openChatRoom(ChatRoom room) {
		return sqlSession.insert("chatMapper.openChatRoom", room);
	}

	
	/** 채팅방 입장 DAO
	 * @param join
	 */
	public void joinChatRoom(ChatRoomJoin join) {
		sqlSession.insert("chatMapper.joinChatRoom", join);
	}

	/** 채팅방 내용 얻어오기 DAO
	 * @param chatRoomNo
	 * @return list
	 */
	public List<ChatMessage> selectChatMessage(int chatRoomNo) {
		return sqlSession.selectList("chatMapper.selectChatMessage", chatRoomNo);
	}

	/** 채팅 내용 삽입
	 * @param cm
	 * @return result
	 */
	public int insertMessage(ChatMessage cm) {
		return sqlSession.insert("chatMapper.insertMessage", cm);
	}
	
	
	
	
}




