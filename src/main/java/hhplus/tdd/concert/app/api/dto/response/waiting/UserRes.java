package hhplus.tdd.concert.app.api.dto.response.waiting;

import hhplus.tdd.concert.app.application.waiting.dto.MemberDTO;

public record UserRes(
        long memberId,
        String memberName,
        Integer charge
) {

    public static UserRes from(MemberDTO dto) {
        return new UserRes(
                dto.memberId(),
                dto.memberName(),
                dto.charge()
        );
    }

}
