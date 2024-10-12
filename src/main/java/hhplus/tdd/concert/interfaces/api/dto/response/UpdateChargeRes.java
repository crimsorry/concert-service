package hhplus.tdd.concert.interfaces.api.dto.response;

import hhplus.tdd.concert.application.dto.PayDto;
import hhplus.tdd.concert.application.dto.UpdateChargeDto;

public record UpdateChargeRes(
        boolean isCharge
) {

    public static UpdateChargeRes from(UpdateChargeDto dto) {
        return new UpdateChargeRes(
                dto.isCharge()
        );
    }

}
