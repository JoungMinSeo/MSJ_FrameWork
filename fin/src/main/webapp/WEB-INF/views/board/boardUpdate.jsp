<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
<style>
  .insert-label {
    display: inline-block;
    width: 80px;
    line-height: 40px
  }
   
  .boardImg{
  	cursor : pointer;
		width: 200px;
		height: 200px;
		border : 1px solid #ced4da;
		position : relative;
	}
	
	.thubnail{
		width: 300px;
		height: 300px;
	}
	
	.boardImg > img{
		max-width : 100%;
		max-height : 100%;
		position: absolute;
		top: 0;
		bottom : 0;
		left : 0;
		right : 0;
		margin : auto;
	}
	
	
	#fileArea{
		display : none;
	}
	
	
	/* 이미지 삭제 버튼  */
	.deleteImg{
		position : absolute;
		top : 5px;
		right : 5px;
		display: inline-block;
		width: 20px;
		height: 20px;
		text-align: center;
		background-color : rgba(50,50,50,0.2);
		border-radius: 50%;
		line-height: 15px;
		font-weight: bold;
		cursor: pointer;
	}
	
	.deleteImg:hover{
		background-color : rgba(50,50,50,0.3);
	}
	
	
</style>
</head>
<body>
		<jsp:include page="../common/header.jsp"></jsp:include>

		<div class="container my-5">

			<h3>게시글 수정</h3>
			<hr>
			
			
			<!-- 
				- enctype : form 태그 데이터가 서버로 제출 될 때 인코딩 되는 방법을 지정. (POST 방식일 때만 사용 가능)
				- application/x-www-form-urlencoded : 모든 문자를 서버로 전송하기 전에 인코딩 (form태그 기본값)
				- multipart/form-data : 모든 문자를 인코딩 하지 않음.(원본 데이터가 유지되어 이미지, 파일등을 서버로 전송 할 수 있음.) 
			-->
			<form action="update" method="post" 
				  enctype="multipart/form-data" role="form" onsubmit="return boardValidate();">
				<c:if test="${ !empty category}"> 
					<div class="mb-2">
						<label class="input-group-addon mr-3 insert-label">카테고리</label> 
						<select	class="custom-select" id="categoryCode" name="categoryCode" style="width: 150px;">
							<c:forEach items="${category}" var="c">
								<option value="${c.categoryCode }">${c.categoryName }</option>
							</c:forEach>
						</select>
					</div>
				</c:if>
				
				
				<div class="form-inline mb-2">
					<label class="input-group-addon mr-3 insert-label">제목</label> 
					<input type="text" class="form-control" id="boardTitle" name="boardTitle" size="70" value="${board.boardTitle}">
				</div>

				<div class="form-inline mb-2">
					<label class="input-group-addon mr-3 insert-label">작성자</label>
					<h5 class="my-0" id="writer">${board.memberName}</h5>
				</div>


				<div class="form-inline mb-2">
					<label class="input-group-addon mr-3 insert-label">작성일</label>
					<h5 class="my-0" id="today">${board.createDate }</h5>
				</div>

				<hr>


				<!-- 이미지 출력 -->
				<c:forEach items="${board.atList}" var="at">
				
					<c:choose>
						<c:when test="${at.fileLevel == 0 && !empty at.fileName}">
							<c:set var="img0" value="${contextPath}/${at.filePath}${at.fileName}"/>
						</c:when>
						<c:when test="${at.fileLevel == 1 && !empty at.fileName}">
							<c:set var="img1" value="${contextPath}/${at.filePath}${at.fileName}"/>
						</c:when>
						<c:when test="${at.fileLevel == 2 && !empty at.fileName}">
							<c:set var="img2" value="${contextPath}/${at.filePath}${at.fileName}"/>
						</c:when>
						<c:when test="${at.fileLevel == 3 && !empty at.fileName}">
							<c:set var="img3" value="${contextPath}/${at.filePath}${at.fileName}"/>
						</c:when>
					</c:choose>
				</c:forEach>

					<div class="form-inline mb-2">
						<label class="input-group-addon mr-3 insert-label">썸네일</label>
						<div class="boardImg thubnail" id="titleImgArea">
							
							<!-- img0 변수가 만들어진 경우 -->
							<c:if test="${!empty img0 }">  <img id="titleImg" src="${img0}"> </c:if>
							<c:if test="${empty img0 }">  <img id="titleImg"> </c:if>
							
							<span class="deleteImg">x</span>
								
						</div>
					</div>
	
					<div class="form-inline mb-2">
						<label class="input-group-addon mr-3 insert-label">업로드<br>이미지</label>
						<div class="mr-2 boardImg" id="contentImgArea1">
							<c:if test="${!empty img1 }">  <img id="titleImg" src="${img1}"> </c:if>
							<c:if test="${empty img1 }">  <img id="titleImg"> </c:if>
							<span class="deleteImg">x</span>
						</div>
	
						<div class="mr-2 boardImg" id="contentImgArea2">
							<c:if test="${!empty img2 }">  <img id="titleImg" src="${img2}"> </c:if>
							<c:if test="${empty img2 }">  <img id="titleImg"> </c:if>
							<span class="deleteImg">x</span>
						</div>
	
						<div class="mr-2 boardImg" id="contentImgArea3">
							<c:if test="${!empty img3 }">  <img id="titleImg" src="${img3}"> </c:if>
							<c:if test="${empty img3 }">  <img id="titleImg"> </c:if>
							<span class="deleteImg">x</span>
						</div>
					</div>


				<!-- 파일 업로드 하는 부분 -->
				<div id="fileArea">
					<input type="file" id="img0" name="images" onchange="LoadImg(this,0)" accept="image/*"> 
					<input type="file" id="img1" name="images" onchange="LoadImg(this,1)" accept="image/*"> 
					<input type="file" id="img2" name="images" onchange="LoadImg(this,2)" accept="image/*"> 
					<input type="file" id="img3" name="images" onchange="LoadImg(this,3)" accept="image/*">
				</div>

				<div class="form-group">
					<div>
						<label for="content">내용</label>
					</div>
					<textarea class="form-control" id="boardContent" name="boardContent" rows="15" style="resize: none;">${board.boardContent }</textarea>
				</div>


				<hr class="mb-4">

				<div class="text-center">
					<button type="submit" class="btn btn-primary">수정</button>
					<a class="btn btn-primary" href="list?cp=${param.cp}">목록으로</a>
					<a class="btn btn-primary" href="${param.boardNo}?cp=${param.cp}">이전</a>
				</div>


				<input type="hidden" name="cp" value="${param.cp}">
				<input type="hidden" name="boardNo" value="${board.boardNo}">

				<!-- 삭제된 이미지를 저장할 태그 추가 -->
				<input type="hidden" name="deleteImages" value="">
			
			</form>
		</div>

		<jsp:include page="../common/footer.jsp"></jsp:include>
		
		
	<script>

		// 유효성 검사 
		function boardValidate() {
			if ($("#boardTitle").val().trim().length == 0) {
				alert("제목을 입력해 주세요.");
				$("#title").focus();
				return false;
			}

			if ($("#boardContent").val().trim().length == 0) {
				alert("내용을 입력해 주세요.");
				$("#content").focus();
				return false;
			}
			
			// 유효성 검사를 모두 통과한 경우
			// input type="hidden" 태그 중 name 속성값이 "deleteImages"인 요소에
			// deleteImages 배열을 value로 추가
			$("input[name='deleteImages']").val(deleteImages);
			
			
		}

		
		
		
		// 이미지 영역을 클릭할 때 파일 첨부 창이 뜨도록 설정하는 함수
		$(function() {
			$(".boardImg").on("click", function() {
				var index = $(".boardImg").index(this);
				$("#img" + index).click();
			});

		});

		
		// 각각의 영역에 파일을 첨부 했을 경우 미리 보기가 가능하도록 하는 함수
		function LoadImg(value, num) {
			if (value.files && value.files[0]) {
				var reader = new FileReader();
				// 자바스크립트 FileReader
				// 웹 애플리케이션이 비동기적으로 데이터를 읽기 위하여 읽을 파일을 가리키는 File 혹은 Blob객체를 이용해 파일의 내용을 읽고 사용자의 컴퓨터에 저장하는 것을 가능하게 해주는 객체

				reader.readAsDataURL(value.files[0]);
				// FileReader.readAsDataURL()
				// 지정된의 내용을 읽기 시작합니다. Blob완료되면 result속성 data:에 파일 데이터를 나타내는 URL이 포함 됩니다.

				// FileReader.onload
				// load 이벤트의 핸들러. 이 이벤트는 읽기 동작이 성공적으로 완료 되었을 때마다 발생합니다.
				reader.onload = function(e) {
					//console.log(e.target.result);
					// e.target.result
					// -> 파일 읽기 동작을 성공한 객체에(fileTag) 올라간 결과(이미지 또는 파일)

					$(".boardImg").eq(num).children("img").attr("src", e.target.result);
					
					//***************************************************
					// 이미지가 추가된 경우 추가된 레벨이 deleteImages 배열에 있는지 검사하여
					// 있을 경우 해당 배열 요소를 제거
					
					const index = deleteImages.indexOf(num);
					// 배열.indexOf("값") : 배열 내부에 값이 일치하는 요소가 있을 경우 해당 인덱스를 반환. 없으면 -1 반환
					
					if(index != -1){ // 배열에 일치하는 레벨이 존재하는 경우
						
						deleteImages.splice( index, 1 );
						// 배열.splice(시작인덱스, 제거할 요수 수)
						// : 배열 내부 요소 중 특정 인덱스부터 시작하여 
						//   지정된 개수 만큼의 요소를 제거
						
					}
					//***************************************************
				}
			}
			
			
		}
			
		// 카테고리를 이전 게시글에 입력된 값으로 세팅
		// == 알맞은 option 태그에 selected 속성을 추가
		
		// 이전 게시글에 입력된 카테고리명
		const categoryName = "${board.categoryName}";
		
		// 다수의 요소를 선택 -> 배열로 반환 
		// 배열 요소에 순차접근하여 하나씩 비교
		// --> 반복문, if(비교)
		$("#categoryCode > option").each(function(index, item){
			// item : 현재 접근한 배열 요소 == 여기서는 option 태그 하나
			
			// $(item) : 현재 접근한 option태그를 선택
			if( $(item).text() == categoryName  ){
				// option 태그에 써져있는 내용(카테고리명)이
				// 이전 게시글의 카테고리명과 같다면
				
				$(item).prop("selected", true);
				// 해당 option 태그에 selected 속성을 추가
			}
		});
			
		
		// 이미지 x 버튼을 눌렀을 때의 동작
		// ****************************************************
		
		// 1) x버튼이 눌러진 이미지 레벨을 저장할 배열 생성
		let deleteImages = [];
		
		// 2) x버튼이 클릭 되었을 때 라는 이벤트 생성
		$(".deleteImg").on("click", function(event){
			// 매개변수 event : 현재 발생한 이벤트와 관련된 모든 정보를 가지고 있는 객체
			event.stopPropagation(); // 이벤트 버블링(이벤트가 연달아 시작되는 것) 삭제
			
			
			//console.log( $(this).prev().attr("src")  ); // img의 src속성값 반환
			
			if(   $(this).prev().attr("src")  != undefined  ){ // 이미지가 있을 경우에만 x버튼 동작을 취하라
			
				// 3) 4개의 .deleteImg 중 몇 번째 인덱스의 x버튼이 눌러졌는지 확인
				// 왜? index == 0,1,2,3 으로 존재 == fileLevel
				const index = $(this).index(".deleteImg");
					// $(this) : 클릭된 x버튼
					//  $(this).index(".deleteImg") : 클릭된 x버튼이 .deleteImg 중 몇 번째 인덱스인지 반환
				        
				//console.log(index);
					
				// 4) deleteImages 배열에 index를 추가
				deleteImages.push(index);
				   
				// 5) x버튼이 눌러진 곳의 이미지 삭제
				$(this).prev().removeAttr("src"); // 클릭한 x버튼의 이전 요소의 속성 중 src 제거
				
				// ★긴급★ AS  
				// boardUpdate.jsp <script> 태그 중
				// $(".deleteImg").on("click", function(event)  제일 마지막에 추가
						
				// 새로 이미지를 올린 후 삭제를해도 input type="file" 태그에는 업로드된 파일 정보가 남아있음
				// -> 이를 제거하지 않으면 파일이 업로드되는 문제 발생!
				// --> 이를 해결하기 위해 눌러진 x버튼과 같은 index의 file 타입 태그의 값을 초기화함.
				$("input[name='images']").eq(index).val("");
				
			}
		});
		
		
		
		
		
		
		
	</script>
	
	
	
	
	
	
	
	
	
</body>
</html>
