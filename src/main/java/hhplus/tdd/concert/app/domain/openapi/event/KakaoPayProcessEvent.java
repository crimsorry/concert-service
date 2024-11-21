package hhplus.tdd.concert.app.domain.openapi.event;

import hhplus.tdd.concert.app.application.payment.dto.PayDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoPayProcessEvent{

    private PayDTO payDTO;


}
