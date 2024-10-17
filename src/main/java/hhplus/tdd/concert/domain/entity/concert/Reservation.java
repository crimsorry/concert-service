package hhplus.tdd.concert.domain.entity.concert;

import hhplus.tdd.concert.domain.entity.member.Member;
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
@Comment("예약 정보")
public class Reservation {

    @Id
    @Comment("예약 ID")
    private Long reserveId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Comment("사용자 ID")
    private Member member;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "seat_id")
    @Comment("좌석 ID")
    private ConcertSeat seat;

    @NotNull
    @Comment("콘서트 명")
    @Column(length = 255)
    private String concertTitle;

    @NotNull
    @Comment("콘서트 개최 일")
    @Column
    private LocalDateTime openDate;

    @NotNull
    @Comment("좌석 번호")
    @Column(length = 3)
    private String seatNum;

    @NotNull
    @Comment("좌석 금액")
    @Column
    private Integer amount;

    @NotNull
    @Comment("예약 상태 (PENDING, RESERVED, CANCELED)")
    @Column
    @Enumerated(EnumType.STRING)
    private ReserveStatus reserveStatus;

    public static Reservation generateReservation(Member member, ConcertSeat seat){
        Reservation reservation = Reservation.builder()
                .member(member)
                .seat(seat)
                .concertTitle(seat.getSchedule().getConcert().getConcertTitle())
                .openDate(seat.getSchedule().getOpenDate())
                .seatNum(seat.getSeatNum())
                .amount(seat.getAmount())
                .reserveStatus(ReserveStatus.PENDING)
                .build();
        return reservation;
    }

}