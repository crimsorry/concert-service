package hhplus.tdd.concert.interfaces.api.dto.response.waiting;

import hhplus.tdd.concert.application.dto.waiting.WaitingNumDto;

public record WaitingNumRes(
        int num
) {

    public static WaitingNumRes from(WaitingNumDto dto) {
        return new WaitingNumRes(
                dto.num()
        );
    }

}
