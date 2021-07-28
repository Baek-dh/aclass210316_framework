package edu.kh.fin.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.fin.board.model.dao.BoardDAO;
import edu.kh.fin.board.model.vo.Board;
import edu.kh.fin.board.model.vo.Category;
import edu.kh.fin.board.model.vo.Pagination;

@Service
public class BoardServiceImpl implements BoardService{

	@Autowired
	private BoardDAO dao;

	// 전체 게시글 수 + 게시판 이름 조회
	@Override
	public Pagination getPagination(Pagination pg) {
		
		// 1) 전체 게시글 수 조회
		Pagination selectPg = dao.getListCount(pg.getBoardType());
		
		// 2) 계산이 완료된 Pagination 객체 생성 후 반환
		return new Pagination(pg.getCurrentPage(), selectPg.getListCount(),
							pg.getBoardType(), selectPg.getBoardName());
	}

	
	// 게시글 목록 조회
	@Override
	public List<Board> selectBoardList(Pagination pagination) {
		return dao.selectBoardList(pagination);
	}


	// 게시글 상세 조회
	@Transactional(rollbackFor = Exception.class) // 모든 예외 발생시 롤백
	@Override
	public Board selectBoard(int boardNo) {
		// 1) 게시글 상세 조회
		Board board =  dao.selectBoard(boardNo);
		
		// 2) 게시글 상세 조회 성공 시 조회수 1 증가
		if(board != null) {
			dao.increaseReadCount(boardNo);
			
			// 3) 조회된 board의 readCount와 DB의 READ_COUNT 동기화
			board.setReadCount(  board.getReadCount() + 1  );
		}
		
		
		return board;
	}

	// 카테고리 목록 조회
	@Override
	public List<Category> selectCategory() {
		return dao.selectCategory();
	}
	
	
	
	
	
	
	
	
	
	
	
}
