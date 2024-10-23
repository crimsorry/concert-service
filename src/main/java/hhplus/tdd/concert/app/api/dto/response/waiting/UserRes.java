package hhplus.tdd.concert.app.api.dto.response.waiting;

import hhplus.tdd.concert.app.application.dto.waiting.MemberQuery;

public record UserRes(
        long memberId,
        String memberName,
        Integer charge
) {

    public static UserRes from(MemberQuery dto) {
        return new UserRes(
                dto.memberId(),
                dto.memberName(),
                dto.charge()
        );
    }

}
