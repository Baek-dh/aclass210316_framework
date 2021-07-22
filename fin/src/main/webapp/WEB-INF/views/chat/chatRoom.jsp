<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>채팅방</title>
<style>
	.chatting-area{ 
		margin:auto;
		height: 700px;  
		width : 500px;
		margin-bottom: 100px;
		}
	.display-chatting{
		width: 100%;
		height: 600px;
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
	
	${list }
	
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
	
	<!-- https://github.com/sockjs/sockjs-client -->
	<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
	

	<script>
		// -------------------------------------------- WebSocket ----------------------------------------------
	
		//let chattingSock; // SockJS를 이용한 WebSocket 객체 저장 변수 선언
	
		// 로그인이 되어 있을 경우에만
		// /chat 이라는 요청 주소로 통신할 수 있는  WebSocket 객체 생성
		let chattingSock = new SockJS("<c:url value='/chat' />");
		
		const memberNo = "${loginMember.memberNo}";
		const memberId = "${loginMember.memberId}";
		const memberName = "${loginMember.memberName}";
		const chatRoomNo = "${chatRoomNo}";
		
		$("#send").on("click", function(){
				const chat = $("#inputChatting").val();
				
				if(chat.trim().length == 0){
					alert("채팅을 입력해주세요.");
				}else{
					var obj = { "memberNo" : memberNo,  
											"memberId" : memberId,
											"memberName" : memberName,
											"chat" : chat,
											"chatRoomNo" : chatRoomNo};
					
					// JSON.stringify() : 자바스크립트 객체를 JSON 문자열로 변환
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
			span.html(new Date());
			
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
