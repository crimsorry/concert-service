package hhplus.tdd.concert.app.application.payment.dto;

import hhplus.tdd.concert.app.domain.reservation.entity.Reservation;
import hhplus.tdd.concert.app.domain.payment.entity.Payment;

import java.time.LocalDateTime;

public record PayDTO(
        long payId,
        long userId,
        long reserveId,
        int amount,
        boolean isPay,
        LocalDateTime createAt
) {

    public static PayDTO from(Payment payment, Reservation reservation) {
        return new PayDTO(
                payment.getPayId(),
                reservation.getMember().getMemberId(),
                reservation.getReserveId(),
                payment.getAmount(),
                payment.getIsPay(),
                payment.getCreateAt()
        );
    }

}
