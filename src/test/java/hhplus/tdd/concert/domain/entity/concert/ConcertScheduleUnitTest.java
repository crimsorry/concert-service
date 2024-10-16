package hhplus.tdd.concert.domain.entity.concert;

import hhplus.tdd.concert.domain.exception.ErrorCode;
import hhplus.tdd.concert.domain.exception.FailException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ConcertScheduleUnitTest {

    @Test
    public void 스케줄_존재_안함_확인(){
        // given
        ConcertSchedule concertSchedule = null;

        // when & then
        Exception exception = assertThrows(FailException.class, () -> {
            ConcertSchedule.checkConcertScheduleExistence(concertSchedule);
        });

        // 결과 검증
        assertEquals(ErrorCode.NOT_FOUND_CONCERT_SCHEDULE.getMessage(), exception.getMessage());
    }

}
