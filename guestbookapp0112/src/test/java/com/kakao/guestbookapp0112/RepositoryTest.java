package com.kakao.guestbookapp0112;

import com.kakao.guestbookapp0112.domain.Board;
import com.kakao.guestbookapp0112.domain.Member;
import com.kakao.guestbookapp0112.domain.Reply;
import com.kakao.guestbookapp0112.persistence.BoardRepository;
import com.kakao.guestbookapp0112.persistence.MemberRepository;
import com.kakao.guestbookapp0112.persistence.ReplyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RepositoryTest {
    // Autowired로 주입받기
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    // 회원 데이터 삽입
    public void insertMember() {
        for(int i = 1; i <= 100; i++) {
            Member member = Member.builder()
                    .email("user" + i + "@kakao.com")
                    .password("1111")
                    .name("USER" + i)
                    .build();
            memberRepository.save(member);
        }
    }

    @Test
    // 게시글 데이터 삽입
    public void insertBoard() {
        for(int i = 1; i <= 100; i++) {
            // Member를 만들고 Board를 만들어가야한다.
            Member member = Member.builder()
                    .email("user" + i + "@kakao.com")
                    .build();
            Board board = Board.builder()
                    .title("제목..." + i)
                    .content("내용..." + i)
                    .writer(member)
                    .build();
            boardRepository.save(board);
        }
    }

    @Test
    // 댓글 데이터 삽입
    public void insertReply() {
        for (int i = 1; i <= 300; i++) {
            long bno = (long) (Math.random() * 100) + 1;
            Board board = Board.builder()
                    .bno(bno)
                    .build();

            Reply reply = Reply.builder()
                    .text("댓글..." + i)
                    .board(board)
                    .replyer("guest")
                    .build();
            replyRepository.save(reply);
        }
    }
}
