package hhplus.tdd.concert.interfaces.api.dto.response;

import hhplus.tdd.concert.application.dto.LoadAmountDto;
import hhplus.tdd.concert.application.dto.UpdateChargeDto;

public record LoadAmountRes(
        int amount
) {

    public static LoadAmountRes from(LoadAmountDto dto) {
        return new LoadAmountRes(
                dto.amount()
        );
    }

}
