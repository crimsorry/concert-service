package hhplus.tdd.concert.interfaces.api.dto.response.waiting;

import hhplus.tdd.concert.application.dto.waiting.WaitingTokenDto;

public record WaitingTokenRes(
        String waitingToken
) {

    public static WaitingTokenRes from(WaitingTokenDto dto) {
        return new WaitingTokenRes(
                dto.waitingToken()
        );
    }

}
