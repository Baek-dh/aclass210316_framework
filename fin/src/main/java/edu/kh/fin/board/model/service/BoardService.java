package edu.kh.fin.board.model.service;

import java.util.List;

import edu.kh.fin.board.model.vo.Board;
import edu.kh.fin.board.model.vo.Category;
import edu.kh.fin.board.model.vo.Pagination;

public interface BoardService {

	/** 전체 게시글 수 + 게시판 이름 조회
	 * @param pg
	 * @return pagination
	 */
	Pagination getPagination(Pagination pg);

	/** 게시글 목록 조회
	 * @param pagination
	 * @return boardList
	 */
	List<Board> selectBoardList(Pagination pagination);

	/** 게시글 상세 조회
	 * @param boardNo
	 * @return board
	 */
	Board selectBoard(int boardNo);

	/** 카테고리 목록 조회
	 * @return category
	 */
	List<Category> selectCategory();
	
	
	

}
