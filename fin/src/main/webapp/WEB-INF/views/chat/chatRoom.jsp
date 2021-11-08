<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
String ipAddress=request.getRemoteAddr();
System.out.println("클라이언트 IP 주소: "+ipAddress);
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>채팅방</title>
<style>
	.chatting-area{ 
		margin:auto;
		height: 600px;  
		width : 800px;
		margin-bottom: 50px;
		margin-top: 50px;
		
		}
	.display-chatting{
		width: 100%;
		height: 450px;
		border : 1px solid black;
		overflow: auto;
		list-style : none;
		padding : 10px 10px;
	}
	
	.chat{
		display: inline-block;
		border-radius: 20%;
		padding : 5px;
		background-color: #eee;
	}
	
	
	.input-area{
		width: 100%;
		display: flex;
	}
	
	#inputChatting{
		width : 80%;
		resize : none;
	}
	
	#send{
		width : 20%;
	}
	
	.myChat{
		text-align: right;
	}
	
	.myChat > p{
		background-color: yellow;
	}
	
	.chatDate{
		font-size: 9px;
	}
	
</style>
</head>
<body>
	<jsp:include page="../common/header.jsp"></jsp:include>
	
	
	<div class="chatting-area">
		<ul class="display-chatting">
	
			<c:forEach items="${list}" var="msg">
				<fmt:formatDate var="chatDate" value="${msg.createDate }" pattern="yyyy년 MM월 dd일 HH:mm:ss"/>
				<c:if test="${msg.memberNo == loginMember.memberNo }">
					<li class="myChat">
						<span class="chatDate">${chatDate}</span>
						<p class="chat">${msg.message }</p>
					</li>
				</c:if>
				
				<c:if test="${msg.memberNo != loginMember.memberNo }">
					<li>
						<b>${msg.memberName }</b>	<br>
						<p class="chat">${msg.message }</p>
						<span class="chatDate">${chatDate}</span>
					</li>
				</c:if>
			
			</c:forEach>
	
			
			
			
		</ul>	
			
	
	
		<div class="input-area">
			<textarea id="inputChatting" rows="3"></textarea>
			<button id="send">보내기</button>
		</div>
	</div>
	
	<jsp:include page="../common/footer.jsp"></jsp:include>
	
	
	<!--------------------------------------- sockjs를 이용한 WebSocket 구현을 위해 라이브러리 추가 ---------------------------------------------->
	
	<!-- 
		WebSocket : 브라우저와 웹서버간의 전이중통신을 지원하는 프로토콜
		(WebSocket을 위한 객체는 HTTP를 이용해 생성되지만, 생성 후 동작은 HTTP와 별도로 진행됨)
		
		(전이중(Full Duplex)  통신 : 두 단말기가 데이터를 송수신 하기 위해 각각 독립된 회선을 사용하는 것)
	
	
		WebSocket을 사용하기 위한 객체를 Javascript가 기본적으로 제공하고 있지만
		웹소켓을 제공하지 않는 브라우저도 있다. 
		-> SockJS 를 이용하면 문제가 해결됨.
		
		SockJS : 브라우저가 WebScoket을 제공하지 않을 때 비슷한 다른 객체를 찾아서 
				 웹소켓 통신이 가능하도록 하는 Javascript 라이브러리.
		
	 -->
	
	
	<!-- https://github.com/sockjs/sockjs-client -->
	<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
	

	<script>
		$(".display-chatting").scrollTop($(".display-chatting")[0].scrollHeight);
		// -------------------------------------------- WebSocket ----------------------------------------------
	
		//let chattingSock; // SockJS를 이용한 WebSocket 객체 저장 변수 선언
	
		// 로그인이 되어 있을 경우에만
		// /chat 이라는 요청 주소로 통신할 수 있는  WebSocket 객체 생성
		
		let chattingSock = new SockJS("${contextPath}/chat");
		
		// -> 생성된 SockJS객체는 자바스크립트(브라우저에서 동작 == 클라이언트)
		//    /chat 주소로 오는 웹소켓 요청을 받아들이는 핸들러는 자바(서버에서 동작 == 서버)
		
		const memberNo = "${loginMember.memberNo}"; // 회원 번호
		const memberId = "${loginMember.memberId}"; // 회원 아이디
		const memberName = "${loginMember.memberName}"; // 회원 이름
		const chatRoomNo = "${chatRoomNo}"; // 채팅방 번호
		
		// 채팅 보내기 버튼을 클릭했을 때
		$("#send").on("click", function(){
				const chat = $("#inputChatting").val(); // 입력한 채팅 내용
				
				if(chat.trim().length == 0){
					alert("채팅을 입력해주세요.");
				
				}else{
					
					// 자바스크립트 객체 obj를 만들어 로그인한 회원의 정보, 채팅방 번호, 채팅 내용을 삽입
					var obj = { "memberNo" : memberNo,  
								"memberId" : memberId,
								"memberName" : memberName,
								"chat" : chat,
								"chatRoomNo" : chatRoomNo};
					
					// JSON : 자바스크립트 객체 표기법
					//        자바스크립트 객체 모양의 문자열
					
					// JSON.stringify() : 자바스크립트 객체를 JSON 문자열로 변환
					
					// 웹소켓 통신 객체를 이용해 /chat 주소로 obj를 JSON으로 변환한 문자열을 전송함
					chattingSock.send(JSON.stringify(obj));
					
					$("#inputChatting").val("");
				}
		});
		
		
		// WebSocket 객체 chattingSock이 서버로 부터 메세지를 통지 받으면 자동으로 실행될 콜백 함수
		chattingSock.onmessage = function(event){
			// 메소드를 통해 전달받은 객체값을 JSON객체로 변환해서 obj 변수에 저장.
			const obj = JSON.parse(event.data);
			console.log(obj);
			
			const li = $("<li>");
			const p = $("<p class='chat'>");
			const span = $("<span class='chatDate'>");
			span.html(obj.createDate);
			
			const chat = obj.chat.replace(/\n/g, "<br>");	
			p.html(chat);
			
			
			if(obj.memberNo == memberNo){
				li.addClass("myChat");
				li.append(span);								
				li.append(p);
			}else{
				li.html("<b>" + obj.memberName + "</b><br>");
				li.append(p);
				li.append(span);								
			}
			
			$(".display-chatting").append(li);
			
			// 채팅 입력 시 스크롤을 가장 아래로 내리기
			$(".display-chatting").scrollTop($(".display-chatting")[0].scrollHeight);

		}	
	</script>
</body>
</html>
