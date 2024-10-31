package hhplus.tdd.concert.app.api.dto.response.payment;

import hhplus.tdd.concert.app.application.payment.dto.LoadAmountQuery;

public record LoadAmountRes(
        int amount
) {

    public static LoadAmountRes from(LoadAmountQuery dto) {
        return new LoadAmountRes(
                dto.amount()
        );
    }

}
