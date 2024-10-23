package hhplus.tdd.concert.app.api.dto.response.waiting;

import hhplus.tdd.concert.app.application.dto.waiting.WaitingNumQuery;

public record WaitingNumRes(
        int num
) {

    public static WaitingNumRes from(WaitingNumQuery dto) {
        return new WaitingNumRes(
                dto.num()
        );
    }

}
