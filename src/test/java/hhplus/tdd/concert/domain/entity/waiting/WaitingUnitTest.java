package hhplus.tdd.concert.domain.entity.waiting;

import hhplus.tdd.concert.domain.entity.member.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class WaitingUnitTest {

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
        assertTrue(result.getExpiredAt().isAfter(LocalDateTime.now()));
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

}
