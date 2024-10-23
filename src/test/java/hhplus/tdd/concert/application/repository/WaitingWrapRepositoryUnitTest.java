package hhplus.tdd.concert.application.repository;

import hhplus.tdd.concert.app.application.repository.WaitingWrapRepository;
import hhplus.tdd.concert.app.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.app.domain.exception.ErrorCode;
import hhplus.tdd.concert.app.domain.repository.waiting.WaitingRepository;
import hhplus.tdd.concert.application.service.TestBase;
import hhplus.tdd.concert.common.config.FailException;
import hhplus.tdd.concert.common.types.WaitingStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WaitingWrapRepositoryUnitTest{

    private final TestBase testBase = new TestBase();

    @InjectMocks
    private WaitingWrapRepository waitingWrapRepository;

    @Mock
    private WaitingRepository waitingRepository;

    @Test
    public void 대기열_토큰_존재_안함() {
        Waiting waiting = null;
        // when
        when(waitingRepository.findByToken(testBase.waitingToken)).thenReturn(waiting);

        // when & then
        Exception exception = assertThrows(FailException.class, () -> {
            waitingWrapRepository.findByTokenOrThrow(testBase.waitingToken);
        });

        // 결과 검증
        assertEquals(ErrorCode.NOT_FOUND_WAITING_MEMBER.getMessage(), exception.getMessage());
    }

    @Test
    public void 대기열_토큰_존재() {
        // when
        when(waitingRepository.findByToken(testBase.waitingToken)).thenReturn(testBase.waiting);

        // then
        Waiting result = waitingWrapRepository.findByTokenOrThrow(testBase.waitingToken);

        // 결과 검증
        assertEquals(testBase.member, result.getMember());
        assertEquals(WaitingStatus.STAND_BY, result.getStatus());
    }

}