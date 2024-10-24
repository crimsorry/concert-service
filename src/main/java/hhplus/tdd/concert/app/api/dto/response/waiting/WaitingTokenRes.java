package hhplus.tdd.concert.app.api.dto.response.waiting;

import hhplus.tdd.concert.app.application.dto.waiting.WaitingTokenCommand;

public record WaitingTokenRes(
        String waitingToken
) {

    public static WaitingTokenRes from(WaitingTokenCommand dto) {
        return new WaitingTokenRes(
                dto.waitingToken()
        );
    }

}
