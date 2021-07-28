package edu.kh.fin.board.model.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.fin.board.model.vo.Board;
import edu.kh.fin.board.model.vo.Category;
import edu.kh.fin.board.model.vo.Pagination;

@Repository
public class BoardDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	/** 특정 게시판 전체 게시글 수 조회
	 * @param boardType
	 * @return pagination
	 */
	public Pagination getListCount(int boardType) {
		return sqlSession.selectOne("boardMapper.getListCount", boardType);
	}

	/** 게시글 목록 조회
	 * @param pagination
	 * @return boardList
	 */
	public List<Board> selectBoardList(Pagination pagination) {
		
		/* 마이바티스의 RowBounds 객체
		 * -> 조회결과 ResultSet에서 어디서 부터 어디까지만 딱 잘라서 얻어올 수 있게하는 객체
		 * 
		 * offset : 조회결과를 몇 행을 건너 뛸 것인지 지정
		 * */
		
		int offset = (pagination.getCurrentPage() - 1) * pagination.getLimit();
		
		RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());
		// offset 만큼 건너 뛰고, limit만큼의 행을 얻어옴
		
		return sqlSession.selectList("boardMapper.selectBoardList", pagination.getBoardType(), rowBounds);
	}

	
	 
	/** 게시글 상세 조회
	 * @param boardNo
	 * @return board
	 */
	public Board selectBoard(int boardNo) {
		return sqlSession.selectOne("boardMapper.selectBoard",boardNo);
	}

	
	/** 조회수 증가
	 * @param boardNo
	 * @return result
	 */
	public int increaseReadCount(int boardNo) {
		return sqlSession.update("boardMapper.increaseReadCount", boardNo);
	}

	/** 카테고리 목록 조회
	 * @return category
	 */
	public List<Category> selectCategory() {
		return sqlSession.selectList("boardMapper.selectCategory");
	}
	
	
	
	
}
