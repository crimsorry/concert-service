package hhplus.tdd.concert.app.interfaces.api.dto.response.payment;

import hhplus.tdd.concert.app.application.payment.dto.LoadAmountDTO;

public record LoadAmountRes(
        int amount
) {

    public static LoadAmountRes from(LoadAmountDTO dto) {
        return new LoadAmountRes(
                dto.amount()
        );
    }

}
