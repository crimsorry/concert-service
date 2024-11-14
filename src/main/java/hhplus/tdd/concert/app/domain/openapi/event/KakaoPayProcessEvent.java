package hhplus.tdd.concert.app.domain.openapi.event;

import hhplus.tdd.concert.app.application.payment.dto.PayDTO;
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
