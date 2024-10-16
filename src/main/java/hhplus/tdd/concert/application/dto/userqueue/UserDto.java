package hhplus.tdd.concert.application.dto.userqueue;

public record UserDto(
        long userId,
        String userName,
        Integer charge
) {
}
