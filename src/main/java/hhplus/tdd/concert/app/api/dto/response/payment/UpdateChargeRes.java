package hhplus.tdd.concert.app.api.dto.response.payment;

import hhplus.tdd.concert.app.application.dto.payment.UpdateChargeCommand;

public record UpdateChargeRes(
        boolean isCharge
) {

    public static UpdateChargeRes from(UpdateChargeCommand dto) {
        return new UpdateChargeRes(
                dto.isCharge()
        );
    }

}
