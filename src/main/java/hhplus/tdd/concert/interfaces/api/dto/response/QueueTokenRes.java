package hhplus.tdd.concert.interfaces.api.dto.response;

import hhplus.tdd.concert.application.dto.QueueTokenDto;

public record QueueTokenRes(
        String queueToken
) {

    public static QueueTokenRes from(QueueTokenDto dto) {
        return new QueueTokenRes(
                dto.queueToken()
        );
    }

}
