package hhplus.tdd.concert.interfaces.api.dto.response;

import hhplus.tdd.concert.application.dto.UserDto;

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
