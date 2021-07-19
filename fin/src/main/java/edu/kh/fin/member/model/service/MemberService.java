package edu.kh.fin.member.model.service;

import edu.kh.fin.member.model.vo.Member;

public interface MemberService {

	/* Service interface를 만드는 이유
	 * 
	 * 1. 프로젝트의 규칙성을 부여하기 위해서
	 * 	-> 인터페이스를 상속 받으면 동일한 형태의 메소드가 강제됨.
	 * 
	 * 2. 클래스간의 결합도를 낮추고, 유지보수성 향상을 위해서
	 * 
	 * 3. Spring의 AOP사용하기 위함
	 *  -> AOP는 spring-proxy를 이용해서 동작하는데
	 *   spring-proxy는 Service 인터페이스를 상속받아 동작함
	 * 
	 * */
	
	// 인터페이스에서 메소드는 모두 묵시적으로 public abstract 이다.
	public abstract Member login(Member inputMember);
	
	
	
	
	
	
}