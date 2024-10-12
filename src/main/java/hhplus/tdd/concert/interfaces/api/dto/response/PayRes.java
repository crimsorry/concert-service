package hhplus.tdd.concert.interfaces.api.dto.response;

import hhplus.tdd.concert.application.dto.PayDto;

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