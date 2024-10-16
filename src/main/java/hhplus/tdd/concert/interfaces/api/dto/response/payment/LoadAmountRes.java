package hhplus.tdd.concert.interfaces.api.dto.response.payment;

import hhplus.tdd.concert.application.dto.payment.LoadAmountDto;

public record LoadAmountRes(
        int amount
) {

    public static LoadAmountRes from(LoadAmountDto dto) {
        return new LoadAmountRes(
                dto.amount()
        );
    }

}
