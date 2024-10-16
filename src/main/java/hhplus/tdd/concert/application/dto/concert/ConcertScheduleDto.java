package hhplus.tdd.concert.application.dto.concert;

import hhplus.tdd.concert.domain.entity.concert.ConcertSchedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ConcertScheduleDto(
        Long scheduleId,
        String concertTitle,
        LocalDateTime openDate,
        LocalDateTime startDate,
        LocalDateTime endDate
) {

    public static ConcertScheduleDto from(ConcertSchedule concertSchedule) {
        return new ConcertScheduleDto(
                concertSchedule.getScheduleId(),
                concertSchedule.getConcert().getConcertTitle(),
                concertSchedule.getOpenDate(),
                concertSchedule.getStartDate(),
                concertSchedule.getEndDate()
        );
    }

    public static List<ConcertScheduleDto> from(List<ConcertSchedule> concertSchedules) {
        return concertSchedules.stream()
                .map(ConcertScheduleDto::from)
                .collect(Collectors.toList());
    }

}
