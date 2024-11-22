package hhplus.tdd.concert.app.domain.openapi.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoMsgEvent {

    private String msg;
    private String memberName;
    private String concertTitle;
    private int amount;
    private String seatCode;

}
