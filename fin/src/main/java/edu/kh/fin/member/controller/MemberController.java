package edu.kh.fin.member.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.fin.member.model.service.MemberService;
import edu.kh.fin.member.model.vo.Member;


@Controller // 프레젠테이션 레이어로 웹 애플리케이션에서 전달된 요청을 받고, 응답 화면을 제어하는
			// 클래스라는 것을 명시 
@RequestMapping("/member/*") // 요청 주소 앞부분이 member로 되어있는 요청을 모두 받아들임.

@SessionAttributes({"loginMember"})
// -> Model 객체에 추가된 속성 중 key 값이 해당 어노테이션에 작성된 값과 같다면 session scope로 이동

public class MemberController {
	
	
	// @Autowired : component-scan을 통해 bean으로 등록된 클래스 중
	// 타입이 같거나, 상속 관계에 있는 객체를 자동으로 DI(의존성 주입) 해줌
	@Autowired
	private MemberService service; 
	
	
	
	// 회원 가입 화면 전환용 Controller
	@RequestMapping("signUp")  // 클래스 위에 작성된 RequestMapping 값 합쳐져 요청을 구분함
	public String signUp() { // "/member/signUp"
		return "member/signUp"; // 요청 위임할 jsp의 이름(ViewResolver 부분을 제외한 나머진 경로)
		
		//   /WEB-INF/views/member/signUp.jsp
	}
	
	
	// -----------------------------------------

	// 5. @ModelAttribute 생략
	@RequestMapping(value="login", method=RequestMethod.POST)
	public String login(Member inputMember, Model model,
						HttpServletRequest request, HttpServletResponse response,
						RedirectAttributes ra, 
						@RequestParam(value="save", required=false) String save ) {
						// required=false -> 아이디 저장을 체크 안할 수도 있기 때문에
						// 필수 입력 상태로 두면 오류가 발생할 것을 예상하여 미리 false로 둠
		
		System.out.println("memberId : " + inputMember.getMemberId());
		System.out.println("memberPw : " + inputMember.getMemberPw());
		System.out.println("save : " + save);
		
		// 의존성 주입(DI)된 service 객체의 기능 중
		// 로그인 서비스를 호출하여 회원 정보를 반환
		Member loginMember = service.login(inputMember);
		
		System.out.println("로그인 결과 : " + loginMember);
		
		// 로그인 == 아이디, 비밀번호가 일치하는 회원 정보를 DB에서 조회해 Session에 올려두는 것
		
		// ** Model 객체 (컨트롤러 메소드의 매개변수로 작성하면 Spring이 알아서 객체를 넣어줌)
		// - 데이터를 맵 형식 (K:V)으로 담아서 전달하는 용도의 객체
		// - Model 객체의 scope는 기본적으로 request이다.
		// (Servlet에서  request.setAttribute("K", V) 를 대신하는 객체)
		
		if(loginMember != null) { // 로그인 성공 시
			
			model.addAttribute("loginMember", loginMember); 
			// 1) model.addAttribute() 만 작성했을 때 == request scope
			// 2) 로그인은 request가 아닌 session에 있어야 되므로 옮겨줘야함!!
			//		-> @SessionAttributes 어노테이션을 이용해야 한다.
			//         (Controller 선언부 위에 작성)
			
			
			// ** 아이디 저장용 Cookie 생성하기
			Cookie cookie = new Cookie("saveId", loginMember.getMemberId());
			
			if(save != null) { // 아이디 저장 체크박스가 체크된 경우
				
				// 쿠키 유지 시간 설정
				cookie.setMaxAge(60 * 60 * 24 * 30); // 한 달 유지(초단위)
			}else {  // 아이디 저장 체크박스가 체크되지 않은 경우
			
				// 쿠키 없애기 == 유지 시간을 0초
				cookie.setMaxAge(0);
			}
			
			// 쿠키 사용 유효 경로 설정
			cookie.setPath( request.getContextPath() ); // 최상위 경로 (/fin) 아래 모든 경로 적용
			
			// 응답에 Cookie를 담아서 클라이언트에게 전달
			response.addCookie(cookie);
			
			
		}else { // 로그인 실패 시
			
			// RedirectAttributes 객체
			// - 리다이렉트 시 값을 전달하는 용도로 사용하는 객체
			// -> 기존 Session 이용 방법의 단점을 보완 
			//    (Session 이용 시 지우기 전까지 유지되는 문제)
			
			// 동작 원리
			// 리다이렉트 전 : request scope 
			// 리다이렉트 중 : session scope
			// 리다이렉트 후 : request socpe
			
			ra.addFlashAttribute("icon", "error");
			ra.addFlashAttribute("title", "로그인 실패");
			ra.addFlashAttribute("text", "아이디 또는 비밀번호가 일치하지 않습니다.");
			
		}
		
		return "redirect:/";
	}
	
	
	//@RequestMapping("logout")
	@RequestMapping(value="logout", method=RequestMethod.GET)
	public String logout(SessionStatus status,
						 @RequestHeader("referer") String referer ) {
						// HTTP Request Header에서 이전 요청 페이지 주소를 저장한 referer의 값을 얻어옴. 
		
		// 로그아웃 : Session에 있는 로그인된 회원 정보를 없애는 것
		
		// @SessionAttributes를 통해 등록된 Session은
		// SessionStatus.setComplete() 를 이용해야지만 없앨 수 있다.
		
		// SessionStatus 객체 : 세션의 상태를 관리할 수 있는 객체
		status.setComplete();
		
		
		
		// 로그아웃 후 로그아웃을 요청한 페이지로 리다이렉트
		// HttpServletRequest 객체에 담긴 header의 내용 중 "referer"을 얻어와야됨.
		// referer : 요청 이전 페이지 주소
		return "redirect:" + referer;
	}
	
	
	// 스프링 예외 처리 방법
	// 1) 메소드 별로 예외 처리 하는 방법 : try-catch,  throws
	
	
	// 2) 컨트롤러 별로 예외 처리 하는 방법 : @ExceptionHandler
	// -> @Controller 또는 @RestController로 등록된 클래스 내부에서
	//   예외가 발생할 경우 처리하는 별도의 메소드
	/*@ExceptionHandler(Exception.class)
	public String exceptionHendler(Exception e, Model model) {
		
		e.printStackTrace();
		model.addAttribute("errorMsg", "서비스 이용 중 문제가 발생했습니다.");
		return "common/error";
	}*/
	
	
	// 3) 전역(모든 클래스)에서 예외 처리 하는 방법 : @ControllerAdvice
	// -> 모든 예외를 모아서 처리하는 컨트롤러를 별도로 생성해서 운영.
	
	
	
	
	
	
	
	
	

}
