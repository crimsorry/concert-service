package hhplus.tdd.concert.interfaces.api.dto.response;

import hhplus.tdd.concert.application.dto.ConcertScheduleDto;

import java.time.LocalDateTime;

public record ConcertScheduleRes(
        Long scheduleId,
        LocalDateTime openDate,
        LocalDateTime startDate,
        LocalDateTime endDate
) {

    public static ConcertScheduleRes from(ConcertScheduleDto concertSchedule) {
        return new ConcertScheduleRes(
                concertSchedule.scheduleId(),
                concertSchedule.openDate(),
                concertSchedule.startDate(),
                concertSchedule.endDate()
        );
    }
}
