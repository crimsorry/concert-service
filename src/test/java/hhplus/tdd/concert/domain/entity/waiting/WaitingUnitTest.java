package hhplus.tdd.concert.domain.entity.waiting;

import hhplus.tdd.concert.app.domain.entity.member.Member;
import hhplus.tdd.concert.app.domain.entity.payment.Payment;
import hhplus.tdd.concert.app.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.common.types.WaitingStatus;
import hhplus.tdd.concert.app.domain.exception.ErrorCode;
import hhplus.tdd.concert.common.config.exception.FailException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class WaitingUnitTest {

    @Test
    public void 존재_하지_않는_대기열(){
        // given
        Waiting waiting = null;

        // when & then
        Exception exception = assertThrows(FailException.class, () -> {
            Waiting.checkWaitingExistence(waiting);
        });

        // 결과 검증
        assertEquals(ErrorCode.NOT_FOUND_WAITING_MEMBER.getMessage(), exception.getMessage());
    }

    @Test
    public void 대기순번_안왔는데_들어옴(){
        // given
        Waiting waiting = new Waiting();
        waiting.setStatus(WaitingStatus.STAND_BY);

        // when & then
        Exception exception = assertThrows(FailException.class, () -> {
            Waiting.checkWaitingStatusActive(waiting);
        });

        // 결과 검증
        assertEquals(ErrorCode.WAITING_MEMBER.getMessage(), exception.getMessage());
    }

    @Test
    public void 토큰_발급_로직(){
        // given
        long memberId = 1L;
        Member member = new Member(memberId, "김소리", 0);
        Waiting waiting = null; // 대기열 없는 상태

        // when & then
        Waiting result = Waiting.generateOrReturnWaitingToken(waiting, member);

        // 결과검증
        assertNotNull(result);
        assertEquals(member, result.getMember());
        assertEquals(WaitingStatus.STAND_BY, result.getStatus());
        assertNotNull(result.getToken());
    }

    @Test
    public void 만료되지_않은_대기열_반환_로직(){
        // given
        long memberId = 1L;
        Member member = new Member(memberId, "김소리", 0);
        Waiting waiting = Waiting.builder()
                .member(member)
                .token("existing-token")
                .status(WaitingStatus.STAND_BY)
                .createAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(30))
                .build();

        // when & then
        Waiting result = Waiting.generateOrReturnWaitingToken(waiting, member);

        // 결과검증
        assertNotNull(result);
        assertEquals(waiting, result);
    }

    @Test
    public void 토큰_생성_확인(){
        // when & then
        String result = Waiting.generateWaitingToken();

        // 결과 검증
        assertNotNull(result);
    }

    @Test
    public void 대기열_만료_상태(){
        // given
        Waiting waiting = new Waiting();
        waiting.setStatus(WaitingStatus.ACTIVE);

        // when & then
        waiting.stop();

        // 결과 검증
        assertEquals(WaitingStatus.EXPIRED, waiting.getStatus());
    }

    @Test
    public void 대기열_순번_온_상태(){
        // given
        Waiting waiting = new Waiting();
        waiting.setStatus(WaitingStatus.STAND_BY);

        // when & then
        waiting.in();

        // 결과 검증
        assertEquals(WaitingStatus.ACTIVE, waiting.getStatus());
    }

    @Test
    public void 대기열_만료_시간_실행(){
        // given
        Waiting waiting = new Waiting();

        // when & then
        waiting.limitPayTime();

        // 결과 검증
        assertNotNull(waiting.getExpiredAt());
    }

}
