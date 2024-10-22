package hhplus.tdd.concert.app.api.dto.response.payment;

import hhplus.tdd.concert.app.application.dto.payment.PayDto;

import java.time.LocalDateTime;

public record PayRes(
        long payId,
        long userId,
        long reserveId,
        int amount,
        boolean isPay,
        LocalDateTime createAt
) {

    public static PayRes from(PayDto dto) {
        return new PayRes(
                dto.payId(),
                dto.userId(),
                dto.reserveId(),
                dto.amount(),
                dto.isPay(),
                dto.createAt()
        );
    }

}