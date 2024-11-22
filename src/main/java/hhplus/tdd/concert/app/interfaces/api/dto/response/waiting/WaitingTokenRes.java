package hhplus.tdd.concert.app.interfaces.api.dto.response.waiting;

import hhplus.tdd.concert.app.application.waiting.dto.WaitingTokenDTO;

public record WaitingTokenRes(
        String waitingToken
) {

    public static WaitingTokenRes from(WaitingTokenDTO dto) {
        return new WaitingTokenRes(
                dto.waitingToken()
        );
    }

}
