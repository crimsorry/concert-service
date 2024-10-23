package hhplus.tdd.concert.app.api.dto.response.concert;

import hhplus.tdd.concert.app.application.dto.concert.ConcertScheduleDto;

import java.time.LocalDateTime;

public record ConcertScheduleRes(
        Long scheduleId,
        String concertTitle,
        LocalDateTime openDate,
        LocalDateTime startDate,
        LocalDateTime endDate
) {

    public static ConcertScheduleRes from(ConcertScheduleDto concertSchedule) {
        return new ConcertScheduleRes(
                concertSchedule.scheduleId(),
                concertSchedule.concertTitle(),
                concertSchedule.openDate(),
                concertSchedule.startDate(),
                concertSchedule.endDate()
        );
    }
}