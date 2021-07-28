package edu.kh.fin.board.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.fin.board.model.service.BoardService;
import edu.kh.fin.board.model.vo.Board;
import edu.kh.fin.board.model.vo.Category;
import edu.kh.fin.board.model.vo.Pagination;
import edu.kh.fin.member.controller.MemberController;

@Controller
@RequestMapping("/board/*")
@SessionAttributes({"loginMember"})
public class BoardController {
	
	// @SessionAttributes
	// Session 올리기 : Model.addAttridute("key", value) 에서의 "key" 값을 
	//					@SessionAttributes에 작성
	
	// Session에서 얻어오기 : @ModelAttribute("세션에 올라간 key") 

	@Autowired
	private BoardService service;
	
	
	// 게시글 목록 조회
	
	// @PathVariable 언제 사용? 특정 자원(게시판, 상세조회) 구분할 때 사용
	// 쿼리스트링은 언제 사용? 정렬, 필터링(검색) 할 때 사용
	@RequestMapping("{boardType}/list")
	public String boardList(@PathVariable("boardType") int boardType,
				@RequestParam(value="cp", required=false, defaultValue = "1") int cp,
				Model model, Pagination pg/*페이징 처리에 사용할 비어있는 객체*/) {
		
		// 1) pg에 boardType, cp를 세팅
		pg.setBoardType(boardType);
		pg.setCurrentPage(cp);
		
		// 2) 전체 게시글 수를 조회하여 Pagination 관련 내용을 계산하고 값을 저장한 객체 반환 받기
		Pagination pagination = service.getPagination(pg);
		
		// 3) 생성된 pagination을 이용하여 현재 목록 페이지에 보여질 게시글 목록 조회
		List<Board> boardList = service.selectBoardList(pagination);
		
		// 조회 결과 임시 확인
		/*for(Board b : boardList) {
			System.out.println(b);
		}*/
		
		model.addAttribute("boardList", boardList);
		model.addAttribute("pagination", pagination);
		
		return "board/boardList";
	}
	
	
	
	// 게시글 상세 조회
	@RequestMapping("{boardType}/{boardNo}")
	public String boardView(@PathVariable("boardType") int boardType,
		@PathVariable("boardNo") int boardNo,
		@RequestParam(value="cp", required=false, defaultValue="1") int cp,
		Model model, RedirectAttributes ra) {
		
		// 게시글 상세조회 Service 호출
		Board board = service.selectBoard(boardNo);
		
		if(board != null) { // 상세 조회 성공 시
			model.addAttribute("board", board);
			return "board/boardView";
			
		}else { // 상세 조회 실패 시(해당 게시글 번호의 글이 없는 경우)

			MemberController.swalSetMessage(ra, "error", "게시글 상세 조회 실패", "해당 글이 존재하지 않습니다.");
			return "redirect:list"; //게시글 목록 조회로 리다이렉트
		}
		
	}
	
	
	
	// 게시글 삽입 화면 전환
	@RequestMapping(value="{boardType}/insert", method = RequestMethod.GET)
	public String insertForm(Model model) {
		
		// DB에서 CATEGORY 테이블 내용을 모두 조회해오기
		List<Category> category = service.selectCategory();
		
		model.addAttribute("category", category); 
		// 요청 위임 페이지에서 사용할 수 있도록 데이터 전달
		
		return "board/boardInsert";
	}
	
	
	
	
	
	
	
	
	
	
}
