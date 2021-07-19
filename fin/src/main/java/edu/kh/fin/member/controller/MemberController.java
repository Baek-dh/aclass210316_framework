package edu.kh.fin.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.kh.fin.member.model.service.MemberService;
import edu.kh.fin.member.model.vo.Member;

@Controller // 프레젠테이션 레이어로 웹 애플리케이션에서 전달된 요청을 받고, 응답 화면을 제어하는
			// 클래스라는 것을 명시 
@RequestMapping("/member/*") // 요청 주소 앞부분이 member로 되어있는 요청을 모두 받아들임.
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
	
	/* @RequestMapping 작성 방법
	 * 
	 * 1. 별도의 추가 속성이 없고, 전달 방식이 get/post 둘 중 아무거나 상관 없을 때
	 *   @RequestMapping("매핑URL")
	 * 
	 * 2. 별도 추가 속성이 있거나, 전달 방식의 구분이 필요할 때
	 *   @RequestMapping(value="매핑URL", method=RequestMethod.POST )
	 * 
	 * */
	
	
	// 로그인 Controller
	
	// 1. HttpServletRequest를 이용한 파라미터 전달 받기
	
	//@RequestMapping("login")
	/*@RequestMapping(value = "login" , method = RequestMethod.POST)
	public String login(HttpServletRequest request) {
		
		String memberId = request.getParameter("memberId");
		String memberPw = request.getParameter("memberPw");
		
		System.out.println("memberId : " + memberId);
		System.out.println("memberPw : " + memberPw);
		
		return null;
	}*/
	
	
	// 2. @RequestParam 어노테이션을 이용한 파라미터 전달 받기
	// - request 객체를 이용한 파라미터 전달 어노테이션
	// - 메소드 매개변수 앞에 작성하면 알맞은 파라미터가 자동으로 매개변수에 주입됨.
	
	// [속성]
	// value : 전달 받은 input태그의 name속성 값
	
	// required : 전달 받는 파라미터의 필수 여부(기본값 true)
	// -> required=true 인 상태에서 파라미터가 존재하지 않는다면 400 Bad Request가 발생함.
	
	// defaultValue : 전달 받은 파라미터 중 일치하는게 없을 경우에 대신 대입될 값 
	
	/*@RequestMapping(value="login", method=RequestMethod.POST)
	public String login(  @RequestParam("memberId") String memberId,  
						  @RequestParam("memberPw") String memberPw,
						  @RequestParam(value="cp", required = false, defaultValue = "1") int cp  ) {
	
		System.out.println("memberId : " + memberId);
		System.out.println("memberPw : " + memberPw);
		System.out.println("cp : " + cp);
		
		return null;
	}*/
	
	
	// 3. @RequestParam 어노테이션 생략
	// -> 생략 가능한 조건 : input태그의 name속성 값과 매개변수의 변수명이 일치하면 생략 가능
	// 어노테이션 코드를 생략하는 경우 의미파악, 가독성이 낮아지게 되므로
	// 업무 환경에 따라서 생략을 못하게 규칙으로 정해진 곳도 있다.
	
	/*@RequestMapping(value="login", method=RequestMethod.POST)
	public String login( String memberId, String memberPw  ) {
		System.out.println("memberId : " + memberId);
		System.out.println("memberPw : " + memberPw);
		
		// 요청 위임(forward) : 요청을 그대로 전달하여 대신 응답 화면을 만드는 것
		//						-> 요청 주소가 그대로 유지됨
		// DispatcherServlet -> Controller -> ViewResolver -> jsp
		
		// 재요청(redirect) : 기존 요청을 폐기하고 새롭게 요청을 보내는 것
		//						-> 새 요청 이므로 요청 주소가 바뀌게됨
		// DispatcherServlet -> Controller -> (다시요청) -> DispatcherServlet -> Controller
		
		return "redirect:/"; // 메인 페이지로 리다이렉트
	}*/
	
	
	
	
	// 4. @ModelAttribute 어노테이션을 이용한 파라미터 전달 받기
	// 요청 페이지에서 여러 파라미터가 전달될 때
	// 해당 파라미터의 key값(input 태그의 name속성값)이
	// 특정 객체의 필드명과 같다면
	// 일치하는 객체를 자동 생성하여 필드에 값을 세팅 후 반환
	
	// [사용 조건]
	// 1) VO 내부에 반드시 기본 생성자가 작성되어 있어야 할 것
	// 2) setter가 반드시 있어야함
	// 3) 필드명과 input태그 name속성 값이 같아야함
	
	// @ModelAttribute를 이용하여 파라미터가 자동 추가된 객체를
	// "커맨드 객체"라고 한다.
	/*@RequestMapping(value="login", method=RequestMethod.POST)
	public String login( @ModelAttribute Member inputMember ) {
		
		System.out.println("memberId : " + inputMember.getMemberId());
		System.out.println("memberPw : " + inputMember.getMemberPw());
		
		return "redirect:/";
	}*/
	
	
	// 5. @ModelAttribute 생략
	@RequestMapping(value="login", method=RequestMethod.POST)
	public String login(Member inputMember) {
		
		System.out.println("memberId : " + inputMember.getMemberId());
		System.out.println("memberPw : " + inputMember.getMemberPw());
		
		// 의존성 주입(DI)된 service 객체의 기능 중
		// 로그인 서비스를 호출하여 회원 정보를 반환
		Member loginMember = service.login(inputMember);
		
		System.out.println("로그인 결과 : " + loginMember);
		
		return "redirect:/";
	}
	
	
	
	
	
	
	
	
	

}
