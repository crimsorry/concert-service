package hhplus.tdd.concert.application.dto.waiting;

public record MemberDto(
        long memberId,
        String memberName,
        Integer charge
) {
}
