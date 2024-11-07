package hhplus.tdd.concert.app.application.service.waiting;

import hhplus.tdd.concert.app.application.waiting.dto.WaitingTokenCommand;
import hhplus.tdd.concert.app.application.service.TestBase;
import hhplus.tdd.concert.app.application.waiting.service.WaitingService;
import hhplus.tdd.concert.app.domain.waiting.repository.MemberRepository;
import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

        // when
        when(memberRepository.findByMemberId(memberId)).thenReturn(testBase.member);
        MockitoAnnotations.openMocks(this);

        // then
        WaitingTokenCommand result = waitingService.enqueueMember(memberId);

        // 결과 검증
        assertNotNull(result);
        assertNotNull(result.waitingToken());
    }

    @Test
    public void 유저_대기열_순서_조회() {
        Set<String> mockTokenSet = new HashSet<>();
        mockTokenSet.add("token1:123");
        mockTokenSet.add("token2:456");
        mockTokenSet.add("token3:789");

        // when
        when(waitingRepository.getAllTokens(testBase.WAITING_TOKEN_KEY)).thenReturn(mockTokenSet);
        when(waitingRepository.getWaitingTokenScore(testBase.WAITING_TOKEN_KEY, "token1:123")).thenReturn(0L);
        when(waitingRepository.getWaitingTokenScore(testBase.WAITING_TOKEN_KEY, "token2:456")).thenReturn(1L);
        when(waitingRepository.getWaitingTokenScore(testBase.WAITING_TOKEN_KEY, "token3:789")).thenReturn(2L);

        // then
        long result = waitingService.loadWaiting("token1").num();
        long result2 = waitingService.loadWaiting("token2").num();
        long result3 = waitingService.loadWaiting("token3").num();

        // 결과 검증
        assertEquals(0L, result);
        assertEquals(1L, result2);
        assertEquals(2L, result3);
    }


}
