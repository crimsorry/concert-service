package hhplus.tdd.concert.application.service;

import hhplus.tdd.concert.app.application.service.BaseService;
import hhplus.tdd.concert.app.domain.entity.waiting.Waiting;
import hhplus.tdd.concert.common.types.WaitingStatus;
import hhplus.tdd.concert.app.domain.exception.ErrorCode;
import hhplus.tdd.concert.common.config.FailException;
import hhplus.tdd.concert.app.domain.repository.waiting.WaitingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BaseServiceUnitTest extends TestBase {

    // 테스트용 서브 클래스 생성
    static class TestBaseService extends BaseService {
        public TestBaseService(WaitingRepository waitingRepository) {
            super(waitingRepository);
        }
    }

    @InjectMocks
    private TestBaseService testBaseService;

    @Mock
    private WaitingRepository waitingRepository;

    @Test
    public void 대기열_토큰_존재_안함() {
        Waiting waiting = null;
        // when
        when(waitingRepository.findByToken(waitingToken)).thenReturn(waiting);

        // when & then
        Exception exception = assertThrows(FailException.class, () -> {
            testBaseService.findAndCheckWaiting(waitingToken);
        });

        // 결과 검증
        assertEquals(ErrorCode.NOT_FOUND_WAITING_MEMBER.getMessage(), exception.getMessage());
    }

    @Test
    public void 대기열_토큰_존재() {
        // when
        when(waitingRepository.findByToken(waitingToken)).thenReturn(waiting);

        // then
        Waiting result = testBaseService.findAndCheckWaiting(waitingToken);

        // 결과 검증
        assertEquals(member, result.getMember());
        assertEquals(WaitingStatus.STAND_BY, result.getStatus());
    }


}
