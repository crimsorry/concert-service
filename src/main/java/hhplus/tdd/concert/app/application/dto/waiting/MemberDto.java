package hhplus.tdd.concert.app.application.dto.waiting;

public record MemberDto(
        long memberId,
        String memberName,
        Integer charge
) {
}
