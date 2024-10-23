package hhplus.tdd.concert.app.domain.entity.concert;

import hhplus.tdd.concert.app.domain.exception.ErrorCode;
import hhplus.tdd.concert.common.config.exception.FailException;
import hhplus.tdd.concert.common.types.SeatStatus;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.Comment;
import org.springframework.boot.logging.LogLevel;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Comment("콘서트 좌석")
public class ConcertSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("좌석 ID")
    private Long seatId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    @Comment("스케줄 ID")
    private ConcertSchedule schedule;

    @NotNull
    @Comment("좌석 번호")
    @Column(length = 3)
    private String seatNum;

    @NotNull
    @Comment("좌석 금액")
    @Column
    private Integer amount;

    @NotNull
    @Comment("좌석 점유 여부 (STAND_BY, RESERVED, ASSIGN)")
    @Column
    @Enumerated(EnumType.STRING)
    private SeatStatus seatStatus;

    public static void checkConcertSeatExistence(ConcertSeat concertSeat){
        if(concertSeat == null){
            throw new FailException(ErrorCode.NOT_FOUND_CONCERT_SEAT, LogLevel.INFO);
        }
    }

    public static void checkConcertSeatReserved(ConcertSeat concertSeat){
        if(concertSeat.getSeatStatus() != SeatStatus.RESERVED){
            throw new FailException(ErrorCode.NOT_FOUND_SEAT_RESERVED, LogLevel.INFO);
        }
    }

    public static void checkConcertSeatStatus(ConcertSeat concertSeat){
        if(concertSeat.getSeatStatus() == SeatStatus.ASSIGN){
            throw new FailException(ErrorCode.ASSIGN_SEAT, LogLevel.INFO);
        }else if(concertSeat.getSeatStatus() == SeatStatus.RESERVED){
            throw new FailException(ErrorCode.RESERVED_SEAT, LogLevel.INFO);
        }
    }

    public void open(){
        setSeatStatus(SeatStatus.STAND_BY);
    }

    public void pending(){
        setSeatStatus(SeatStatus.RESERVED);
    }

    public void close(){
        setSeatStatus(SeatStatus.ASSIGN);
    }

}