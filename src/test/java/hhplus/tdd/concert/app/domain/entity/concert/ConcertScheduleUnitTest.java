package hhplus.tdd.concert.app.domain.entity.concert;

import hhplus.tdd.concert.app.domain.entity.concert.ConcertSchedule;
import hhplus.tdd.concert.app.domain.exception.ErrorCode;
import hhplus.tdd.concert.common.config.exception.FailException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
