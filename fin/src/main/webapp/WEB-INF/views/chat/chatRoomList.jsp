<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>채팅방 목록</title>
<style>
/* 목록 내부 td 태그 */
#list-table td{
	padding : 0; /* td 태그 padding을 없앰 */
  vertical-align: middle; /* td태그 내부 세로 가운데 정렬*/
  /* vertical-align : inline, inline-block 요소에만 적용 가능(td는 inline-block)*/
}

/* 컬럼명 가운데 정렬 */
#list-table th, #list-table td {
	text-align: center;
}

/* 목록의 높이가 최소 540px은 유지하도록 설정 */
.list-wrapper{
	min-height: 540px;
}

/* 제목 영역의 너비를 table의 50% 넓게 설정*/
#list-table th:nth-child(2){
	width: 50%;
}

.form-label-group {
  position: relative;
  margin-bottom: 1rem;
}

.form-label-group > input,
.form-label-group > label {
  height: 3.125rem;
  padding: .75rem;
}

.form-label-group > label {
  position: absolute;
  top: 0;
  left: 0;
  display: block;
  width: 100%;
  margin-bottom: 0; /* Override default `<label>` margin */
  line-height: 1.5;
  color: #495057;
  pointer-events: none;
  cursor: text; /* Match the input under the label */
  border: 1px solid transparent;
  border-radius: .25rem;
  transition: all .1s ease-in-out;
}

.form-label-group input::-webkit-input-placeholder {
  color: transparent;
}

.form-label-group input:-ms-input-placeholder {
  color: transparent;
}

.form-label-group input::-ms-input-placeholder {
  color: transparent;
}

.form-label-group input::-moz-placeholder {
  color: transparent;
}

.form-label-group input::placeholder {
  color: transparent;
}

.form-label-group input:not(:placeholder-shown) {
  padding-top: 1.25rem;
  padding-bottom: .25rem;
}

.form-label-group input:not(:placeholder-shown) ~ label {
  padding-top: .25rem;
  padding-bottom: .25rem;
  font-size: 12px;
  color: #777;
}

/* Fallback for Edge
-------------------------------------------------- */
@supports (-ms-ime-align: auto) {
  .form-label-group > label {
    display: none;
  }
  .form-label-group input::-ms-input-placeholder {
    color: #777;
  }
}

/* Fallback for IE
-------------------------------------------------- */
@media all and (-ms-high-contrast: none), (-ms-high-contrast: active) {
  .form-label-group > label {
    display: none;
  }
  .form-label-group input:-ms-input-placeholder {
    color: #777;
  }
}

</style>

</head>
<body>
	<jsp:include page="../common/header.jsp"></jsp:include>
	<div class="container my-5">
		<h1>채팅방 목록</h1>
			<div class="list-wrapper">
				<table class="table table-hover table-striped my-5" id="list-table">
					<thead>
						<tr>
							<th>방번호</th>
							<th>채팅방 제목(주제)</th>
							<th>개설자</th>
							<th>참여인원수</th>
						</tr>
					</thead>
					
					
					<%-- 채팅 목록 출력 --%>
					<tbody>
					
						<c:choose>
						
							<%-- 조회된 게시글 목록이 없을 때 --%>
							<c:when test="${empty chatRoomList }">
								<tr>
									<td colspan="4">존재하는 채팅방이 없습니다.</td>
								</tr>
							</c:when>
							
							
							<%-- 조회된 채팅방 목록이 있을 때 --%>
							<c:otherwise>
								
								<c:forEach var="chatRoom" items="${chatRoomList}">
									<tr>
										<td>${chatRoom.chatRoomNo}</td> <%-- 채팅방번호 --%>
										<td>
											${chatRoom.title}
											
											<c:if test="${!empty loginMember }">
												<a class="btn btn-primary btn-sm" href="${contextPath}/chat/room/${chatRoom.chatRoomNo}">참여</a>
											</c:if>
										</td> <%-- 제목 --%>
										
										<td>${chatRoom.memberName}</td> <%-- 개설자 --%>
										<td>${chatRoom.cnt}</td> <%-- 참여인원수 --%>
									</tr>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
				
				
				<%-- 로그인이 되어있는 경우 --%>
				<c:if test="${!empty loginMember }">
					 <a class="btn btn-primary float-right" data-toggle="modal" href="#openChatRoom">채팅방 만들기</a>
				</c:if>
			</div>
		</div>

	<div class="modal fade" id="openChatRoom" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">

			<div class="modal-content">

				<div class="modal-header">
					<h5 class="modal-title" id="myModalLabel">채팅방 만들기</h5>
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
				</div>

				<div class="modal-body">
					<form method="POST" action="${contextPath}/chat/openChatRoom">

						<div class="form-label-group">
							<input type="text" id="chatRoomTitle" name="title" class="form-control" placeholder="채팅방 제목" required> 
							<label for="chatRoomTitle">채팅방 제목</label>
						</div>
						
						

						<button class="btn btn-lg btn-primary btn-block" type="submit">만들기</button>
						
						
					</form>
				</div>
			</div>
		</div>
	</div>
			
			
	<jsp:include page="../common/footer.jsp"></jsp:include>



	<script>
		
	</script>
</body>
</html>
