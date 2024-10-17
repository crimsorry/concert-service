package hhplus.tdd.concert.interfaces.api.dto.response.userqueue;

import hhplus.tdd.concert.application.dto.waiting.QueueNumDto;

public record QueueNumRes(
        int num
) {

    public static QueueNumRes from(QueueNumDto dto) {
        return new QueueNumRes(
                dto.num()
        );
    }

}
