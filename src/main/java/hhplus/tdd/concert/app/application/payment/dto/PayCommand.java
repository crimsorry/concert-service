package hhplus.tdd.concert.app.application.payment.dto;

import hhplus.tdd.concert.app.domain.reservation.entity.Reservation;
import hhplus.tdd.concert.app.domain.payment.entity.Payment;

import java.time.LocalDateTime;

public record PayCommand(
        long payId,
        long userId,
        long reserveId,
        int amount,
        boolean isPay,
        LocalDateTime createAt
) {

    public static PayCommand from(Payment payment, Reservation reservation) {
        return new PayCommand(
                payment.getPayId(),
                reservation.getMember().getMemberId(),
                reservation.getReserveId(),
                payment.getAmount(),
                payment.getIsPay(),
                payment.getCreateAt()
        );
    }

}
