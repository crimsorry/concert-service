package hhplus.tdd.concert.app.application.concert.dto;

import hhplus.tdd.concert.app.domain.concert.entity.ConcertSchedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ConcertScheduleQuery(
        Long scheduleId,
        String concertTitle,
        LocalDateTime openDate,
        LocalDateTime startDate,
        LocalDateTime endDate
) {

    public static ConcertScheduleQuery from(ConcertSchedule concertSchedule) {
        return new ConcertScheduleQuery(
                concertSchedule.getScheduleId(),
                concertSchedule.getConcert().getConcertTitle(),
                concertSchedule.getOpenDate(),
                concertSchedule.getStartDate(),
                concertSchedule.getEndDate()
        );
    }

    public static List<ConcertScheduleQuery> from(List<ConcertSchedule> concertSchedules) {
        return concertSchedules.stream()
                .map(ConcertScheduleQuery::from)
                .collect(Collectors.toList());
    }

}
