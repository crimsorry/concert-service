package hhplus.tdd.concert.app.domain.entity.concert;

import hhplus.tdd.concert.app.domain.exception.ErrorCode;
import hhplus.tdd.concert.common.config.exception.FailException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.Comment;
import org.springframework.boot.logging.LogLevel;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Comment("콘서트 일정")
public class ConcertSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("스케줄 ID")
    private Long scheduleId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    @Comment("콘서트 ID")
    private Concert concert;

    @NotNull
    @Comment("콘서트 개최 일")
    @Column
    private LocalDateTime openDate;

    @NotNull
    @Comment("티켓 예매 시작 시간")
    @Column
    private LocalDateTime startDate;

    @NotNull
    @Comment("티켓 예매 종료 시간")
    @Column
    private LocalDateTime endDate;

    public static void checkConcertScheduleExistence(ConcertSchedule concertSchedule){
        if(concertSchedule == null){
            throw new FailException(ErrorCode.NOT_FOUND_CONCERT_SCHEDULE, LogLevel.WARN);
        }
    }
}
