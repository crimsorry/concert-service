package hhplus.tdd.concert.app.api.dto.response.waiting;

import hhplus.tdd.concert.app.application.waiting.dto.WaitingNumQuery;

public record WaitingNumRes(
        Long num
) {

    public static WaitingNumRes from(WaitingNumQuery dto) {
        return new WaitingNumRes(
                dto.num()
        );
    }

}
