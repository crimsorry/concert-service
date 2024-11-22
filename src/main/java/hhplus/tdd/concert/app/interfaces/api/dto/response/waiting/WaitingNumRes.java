package hhplus.tdd.concert.app.interfaces.api.dto.response.waiting;

import hhplus.tdd.concert.app.application.waiting.dto.WaitingNumDTO;

public record WaitingNumRes(
        Long num
) {

    public static WaitingNumRes from(WaitingNumDTO dto) {
        return new WaitingNumRes(
                dto.num()
        );
    }

}
