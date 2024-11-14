package hhplus.tdd.concert.app.domain.event;

import hhplus.tdd.concert.app.application.payment.dto.PayDTO;
import hhplus.tdd.concert.app.application.reservation.dto.ReservationDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class KakaoPayProcessEvent extends ApplicationEvent {

    private final PayDTO payDTO;

    public KakaoPayProcessEvent(Object source, PayDTO payDTO) {
        super(source);
        this.payDTO = payDTO;
    }

}
