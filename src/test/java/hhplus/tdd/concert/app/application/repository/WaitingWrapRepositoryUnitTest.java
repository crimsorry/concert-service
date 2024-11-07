//package hhplus.tdd.concert.app.application.repository;
//
//import hhplus.tdd.concert.app.application.service.TestBase;
//import hhplus.tdd.concert.app.domain.waiting.entity.Waiting;
//import hhplus.tdd.concert.app.domain.exception.ErrorCode;
//import hhplus.tdd.concert.app.domain.waiting.repository.WaitingRepository;
//import hhplus.tdd.concert.config.exception.FailException;
//import hhplus.tdd.concert.config.types.WaitingStatus;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class WaitingWrapRepositoryUnitTest{
//
//    private final TestBase testBase = new TestBase();
//
//    @Mock
//    private WaitingRepository waitingRepository;
//
//    @Test
//    public void 대기열_토큰_존재_안함() {
//        Waiting waiting = null;
//        // when
//        when(waitingRepository.findByToken(testBase.waitingToken)).thenReturn(waiting);
//
//        // when & then
//        Exception exception = assertThrows(FailException.class, () -> {
//            waitingRepository.findByToken(testBase.waitingToken);
//        });
//
//        // 결과 검증
//        assertEquals(ErrorCode.NOT_FOUND_WAITING_MEMBER.getMessage(), exception.getMessage());
//    }
//
//    @Test
//    public void 대기열_토큰_존재() {
//        // when
//        when(waitingRepository.findByTokenOrThrow(testBase.waitingToken)).thenReturn(testBase.waiting);
//
//        // then
//        Waiting result = waitingRepository.findByToken(testBase.waitingToken);
//
//        // 결과 검증
//        assertEquals(testBase.member, result.getMember());
//        assertEquals(WaitingStatus.STAND_BY, result.getStatus());
//    }
//
//}