package hhplus.tdd.concert.interfaces.api.dto.response.waiting;

import hhplus.tdd.concert.application.dto.waiting.MemberDto;

public record UserRes(
        long memberId,
        String memberName,
        Integer charge
) {

    public static UserRes from(MemberDto dto) {
        return new UserRes(
                dto.memberId(),
                dto.memberName(),
                dto.charge()
        );
    }

}
