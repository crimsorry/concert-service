package hhplus.tdd.concert.domain.entity.concert;

import hhplus.tdd.concert.domain.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.Comment;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Comment("콘서트 좌석")
public class ConcertSeat {

    @Id
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
}