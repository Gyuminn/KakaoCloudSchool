package com.kakao.guestbookapp0112.persistence;

import com.kakao.guestbookapp0112.domain.Board;
import com.kakao.guestbookapp0112.domain.QBoard;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository{

    // QuerydslRepositorySupport 클래스에 Default Contructor가 없기 때문에
    // Contructor를 직접 생성해서 필요한 Contructor를 호출해줘야 한다. 그렇지 않으면 에러
    // 검색에 사용할 Entity 클래스를 대입해주어야 한다.
    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }
    @Override
    public Board search1() {
        // JPQL을 동적으로 생성해서 실행
        QBoard board = QBoard.board;

        // 쿼리 작성
        JPQLQuery<Board> jpqlQuery = from(board);
        // bno가 1인 데이터를 조회
        jpqlQuery.select(board).where(board.bno.eq(1L));
        // jpql을 실행시킨 결과 가져오기
        List<Board> result = jpqlQuery.fetch();

        return null;
    }
}
