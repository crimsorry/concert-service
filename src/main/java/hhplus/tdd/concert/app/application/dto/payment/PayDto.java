package hhplus.tdd.concert.app.application.dto.payment;

import hhplus.tdd.concert.app.domain.entity.concert.Reservation;
import hhplus.tdd.concert.app.domain.entity.payment.Payment;

import java.time.LocalDateTime;

public record PayDto(
        long payId,
        long userId,
        long reserveId,
        int amount,
        boolean isPay,
        LocalDateTime createAt
) {

    public static PayDto from(Payment payment, Reservation reservation) {
        return new PayDto(
                payment.getPayId(),
                reservation.getMember().getMemberId(),
                reservation.getReserveId(),
                payment.getAmount(),
                payment.getIsPay(),
                payment.getCreateAt()
        );
    }

}
