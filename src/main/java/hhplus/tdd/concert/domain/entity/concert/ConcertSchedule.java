package hhplus.tdd.concert.domain.entity.concert;

import hhplus.tdd.concert.domain.exception.ErrorCode;
import hhplus.tdd.concert.domain.exception.FailException;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Setter
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

    @NotNull
    @Comment("남은 좌석 수")
    @Column
    private Integer capacity;

    public static void checkConcertScheduleExistence(ConcertSchedule concertSchedule){
        if(concertSchedule == null){
            throw new FailException(ErrorCode.NOT_FOUND_CONCERT_SCHEDULE);
        }
    }
}
