package hhplus.tdd.concert.app.application.openapi.dto;

import hhplus.tdd.concert.app.domain.openapi.event.KakaoMsgEvent;

public record KakaoMsgDto(
        String msg,
        String memberName,
        String concertTitle,
        int amount,
        String seatCode
) {

    public static KakaoMsgDto from(KakaoMsgEvent kakaoMsgEvent) {
        return new KakaoMsgDto(
                kakaoMsgEvent.getMsg(),
                kakaoMsgEvent.getMemberName(),
                kakaoMsgEvent.getConcertTitle(),
                kakaoMsgEvent.getAmount(),
                kakaoMsgEvent.getSeatCode()
        );
    }

}
