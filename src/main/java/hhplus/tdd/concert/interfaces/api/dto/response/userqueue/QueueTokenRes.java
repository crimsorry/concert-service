package hhplus.tdd.concert.interfaces.api.dto.response.userqueue;

import hhplus.tdd.concert.application.dto.userqueue.QueueTokenDto;

public record QueueTokenRes(
        String queueToken
) {

    public static QueueTokenRes from(QueueTokenDto dto) {
        return new QueueTokenRes(
                dto.queueToken()
        );
    }

}
