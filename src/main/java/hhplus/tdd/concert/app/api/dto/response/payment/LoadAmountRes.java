package hhplus.tdd.concert.app.api.dto.response.payment;

import hhplus.tdd.concert.app.application.dto.payment.LoadAmountDto;

public record LoadAmountRes(
        int amount
) {

    public static LoadAmountRes from(LoadAmountDto dto) {
        return new LoadAmountRes(
                dto.amount()
        );
    }

}
