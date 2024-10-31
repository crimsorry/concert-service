package hhplus.tdd.concert.app.application.waiting.dto;

public record MemberQuery(
        long memberId,
        String memberName,
        Integer charge
) {
}
