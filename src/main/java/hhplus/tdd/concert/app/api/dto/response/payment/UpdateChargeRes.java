package hhplus.tdd.concert.app.api.dto.response.payment;

import hhplus.tdd.concert.app.application.payment.dto.UpdateChargeDTO;

public record UpdateChargeRes(
        boolean isCharge
) {

    public static UpdateChargeRes from(UpdateChargeDTO dto) {
        return new UpdateChargeRes(
                dto.isCharge()
        );
    }

}
