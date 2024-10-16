package hhplus.tdd.concert.interfaces.api.dto.response.userqueue;

import hhplus.tdd.concert.application.dto.waiting.UserDto;

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
