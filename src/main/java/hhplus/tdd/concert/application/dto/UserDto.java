package hhplus.tdd.concert.application.dto;

public record UserDto(
        long userId,
        String userName,
        Integer charge
) {
}
