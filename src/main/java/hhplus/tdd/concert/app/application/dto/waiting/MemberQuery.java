package hhplus.tdd.concert.app.application.dto.waiting;

public record MemberQuery(
        long memberId,
        String memberName,
        Integer charge
) {
}
