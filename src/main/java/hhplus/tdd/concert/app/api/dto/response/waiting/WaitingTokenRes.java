package hhplus.tdd.concert.app.api.dto.response.waiting;

import hhplus.tdd.concert.app.application.dto.waiting.WaitingTokenDto;

public record WaitingTokenRes(
        String waitingToken
) {

    public static WaitingTokenRes from(WaitingTokenDto dto) {
        return new WaitingTokenRes(
                dto.waitingToken()
        );
    }

}
