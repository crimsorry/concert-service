package hhplus.tdd.concert.application.service.waiting;

import hhplus.tdd.concert.app.application.dto.waiting.WaitingTokenCommand;
import hhplus.tdd.concert.app.application.service.waiting.WaitingService;
import hhplus.tdd.concert.app.domain.entity.member.Member;
import hhplus.tdd.concert.app.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.app.domain.repository.member.MemberRepository;
import hhplus.tdd.concert.app.domain.repository.waiting.WaitingRepository;
import hhplus.tdd.concert.application.service.TestBase;
import hhplus.tdd.concert.common.types.WaitingStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WaitingServiceUnitTest {

    private final TestBase testBase = new TestBase();

    @InjectMocks
    private WaitingService waitingService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private WaitingRepository waitingRepository;

    @Test
    public void 유저_대기열_생성_성공() {
        // given
        long memberId = 1L;
        Member member = new Member(memberId, "김소리", 0);
        Waiting waiting = null; // 대기열이 없는 상태를 가정

        // when
        when(memberRepository.findByMemberId(memberId)).thenReturn(member);
        when(waitingRepository.findByMemberAndStatusNot(member, WaitingStatus.EXPIRED)).thenReturn(waiting);
        when(waitingRepository.save(any(Waiting.class))).thenAnswer(invocation -> {
            Waiting savedQueue = invocation.getArgument(0);
            savedQueue.setWaitingId(1L); // save 후에 ID가 생성됨 가정
            return savedQueue;
        });

        // then
        WaitingTokenCommand result = waitingService.enqueueMember(memberId);

        // 결과 검증
        assertNotNull(result);
        assertNotNull(result.waitingToken());
        verify(memberRepository).findByMemberId(memberId);
        verify(waitingRepository).findByMemberAndStatusNot(member, WaitingStatus.EXPIRED);
        verify(waitingRepository).save(any(Waiting.class));
    }

    @Test
    public void 유저_대기열_순서_조회() {
        // when
        when(waitingRepository.findByToken(testBase.waitingToken)).thenReturn(testBase.waiting);
        when(waitingRepository.countByWaitingIdLessThanAndStatus(testBase.member.getMemberId(), WaitingStatus.STAND_BY)).thenReturn(0);

        // then
        long result = waitingService.loadWaiting(testBase.waitingToken).num();

        // 결과 검증
        assertEquals(0, result);
    }


}
