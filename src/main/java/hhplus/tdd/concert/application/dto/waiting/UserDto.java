package hhplus.tdd.concert.application.dto.waiting;

public record UserDto(
        long userId,
        String userName,
        Integer charge
) {
}
