package hhplus.tdd.concert.app.api.dto.response.payment;

import hhplus.tdd.concert.app.application.dto.payment.UpdateChargeDto;

public record UpdateChargeRes(
        boolean isCharge
) {

    public static UpdateChargeRes from(UpdateChargeDto dto) {
        return new UpdateChargeRes(
                dto.isCharge()
        );
    }

}
