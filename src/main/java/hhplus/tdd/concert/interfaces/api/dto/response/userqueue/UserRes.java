package hhplus.tdd.concert.interfaces.api.dto.response.userqueue;

import hhplus.tdd.concert.application.dto.userqueue.UserDto;

public record UserRes(
        long userId,
        String userName,
        Integer charge
) {

    public static UserRes from(UserDto dto) {
        return new UserRes(
                dto.userId(),
                dto.userName(),
                dto.charge()
        );
    }

}
