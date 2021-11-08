package edu.kh.fin.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.fin.chat.model.service.ChatService;
import edu.kh.fin.chat.model.vo.ChatMessage;
import edu.kh.fin.chat.model.vo.ChatRoom;
import edu.kh.fin.chat.model.vo.ChatRoomJoin;
import edu.kh.fin.member.controller.MemberController;
import edu.kh.fin.member.model.vo.Member;

@Controller
@SessionAttributes( { "loginMember", "chatRoomNo" } )
public class ChatController {

	@Autowired
	private ChatService service;
	
	
	// 채팅방 목록 전환 Controller
	@RequestMapping(value="/chat/roomList", method=RequestMethod.GET)
	public String chatRoomList(Model model) {
		
		// 채팅룸 목록을 조회하여 화면으로 요청 위임
		List<ChatRoom> chatRoomList = service.selectRoomList();
		
		// Model : 데이터 전달용 객체 (기본 request scope)
		model.addAttribute("chatRoomList", chatRoomList);
		
		return "chat/chatRoomList";
		
	}
	
	
	// 채팅방 만들기 Controller
	@RequestMapping(value="/chat/openChatRoom", method=RequestMethod.POST)
	public String openChatRoom( ChatRoom room, 
							   @ModelAttribute("loginMember") Member loginMember,
							   RedirectAttributes ra) {
		// ChatRoom room 매개변수에 파라미터 title이 담김
		
		// 채팅방을 만들 때는 누가, 어떤 이름의 채팅방을 만들었는가를 DB에 저장
		room.setMemberNo( loginMember.getMemberNo()  );
		
		int chatRoomNo = service.openChatRoom(room);
		// chatRoomNo를 반환 받는 이유 == 채팅방 개설 후 바로 들어가기 위해서
		
		String path = "redirect:/chat/";
		
		if(chatRoomNo > 0) { // 채팅방이 만들어진 경우
			path += "room/" + chatRoomNo;
		}else {
			path += "roomList";
			MemberController.swalSetMessage(ra, "error", "채팅방 만들기 실패", null);
		}
		
		return path;
	}
	
	
	// 채팅방 참여 + 채팅방 메세지 조회
	@RequestMapping("/chat/room/{chatRoomNo}")
	public String joinChatRoom( @PathVariable("chatRoomNo") int chatRoomNo,
								@ModelAttribute("loginMember") Member loginMember,
								Model model, ChatRoomJoin join) {
		
		// @PathVariable 어노테이션
		// - URL 경로상에 작성된 값을 파라미터로 사용할 수 있게 하는 어노테이션
		// 언제는 @PathVariable, 언제는 쿼리 스트링을 쓰는가?
		// 페이지가 독립적으로 구분,존재 == @PathVariable
		// 검색, 페이징 == 쿼리스트링
		
		// 비어있는 커맨드 객체 join에 채팅방번호, 회원번호 세팅
		join.setChatRoomNo(chatRoomNo);
		join.setMemberNo(loginMember.getMemberNo());
		
		// 채팅방별참여 테이블에 insert 후 해당 채팅방 내용을 조회해오기
		List<ChatMessage> list = service.joinChatRoom(join);
		
		
		model.addAttribute("list", list);
		
		model.addAttribute("chatRoomNo", chatRoomNo); // 채팅방 번호를 Session에 올리기
		// 왜 올렸는가??
		// -> 웹소켓은 현재 서버에 접속중인 모든 사용자의 Session을 목록으로 받아올 수 있다.
		//   그런데 Session에 chatRoomNo(채팅방 번호)가 있을 경우
		//   특정 채팅방에만 존재하는 사람들이게 메세지를 전달할 수 있다.
		
		return "chat/chatRoom";
	}
	
	
	
	
	
	
	
	
	
	
}
