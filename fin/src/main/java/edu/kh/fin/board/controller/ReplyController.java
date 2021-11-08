package edu.kh.fin.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.kh.fin.board.model.service.ReplyService;
import edu.kh.fin.board.model.vo.Reply;

//@Controller
@RestController // @Contorller + @ResponseBody = 반환되는 내용이 모두 값 자체로 인식되는 컨트롤러

// REST(Representational State Transfer) : HTTP를 이용해서 정보(자원)를 주고받는 인터페이스
// 클라이언트 요청 시 서버가 화면이 아닌 데이터만 내보냄

// REST API : 특정 데이터, 정보를 모아두고 요청에 따라 필요한 정보를 교환 가능하도록 하는 것

// RESTFUL : REST API를 제공하는 애플리케이션, 아키텍처
@RequestMapping("/reply/*")
@SessionAttributes({"loginMember"}) // 댓글 삽입 시 누가 썼는지 알아야함 -> Session에서 얻어와야함
public class ReplyController {
	
	@Autowired
	private ReplyService service;
	
	// 댓글 목록 조회
	@RequestMapping(value="list", method=RequestMethod.POST)
	//@ResponseBody
	public String selectList(int boardNo) {
		
		List<Reply> rList = service.selectList(boardNo);
		
		// Controller의 반환값은 forward 또는 redirect를 위한 경로를 작성함
		// 값 자체를 전달하기 위한 어노테이션 @ResponseBody를 사용해야
		// Ajax 반환값으로 전달 가능
		
		// rList 를 JSON 형태로 변환
		// -> GSON 라이브러리를 이용해서 쉽게 변환
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy년 MM월 dd일 HH:mm").create();
		// gson을 이용한 JSON 변환 시 날짜데이터 형식을 지정함
		
		return gson.toJson(rList);
		// rList를 JSON 형태로 변환하여 반환 (String)
	}
	
	
	// 댓글 삽입
	@RequestMapping(value="insertReply", method=RequestMethod.POST)
	//@ResponseBody
	public int insertReply(Reply reply/*커맨드 객체*/) {
		return service.insertReply(reply);
	}
	
	
	
	
	// 댓글 수정
	// @ResponseBody
	@RequestMapping(value="updateReply", method=RequestMethod.POST)
	public int updateReply(Reply reply) {
		return service.updateReply(reply);
	}
	
	
	// 댓글 삭제
	// @ResponseBody
	@RequestMapping(value="deleteReply", method=RequestMethod.GET)
	public int deleteReply(int replyNo) {
		return service.deleteReply(replyNo);
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
