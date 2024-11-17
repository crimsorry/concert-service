package hhplus.tdd.concert.app.infrastructure.event.external;

import hhplus.tdd.concert.app.application.payment.dto.PayDTO;
import hhplus.tdd.concert.app.application.reservation.dto.ReservationDTO;
import hhplus.tdd.concert.app.domain.openapi.event.KakaoPayProcessEvent;
import hhplus.tdd.concert.app.domain.openapi.event.KakaoProcessPublisher;
import hhplus.tdd.concert.app.domain.openapi.event.KakaoReservationProcessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoProcessPublisherImpl implements KakaoProcessPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publishReservationEvent(ReservationDTO reservationDTO) {
        eventPublisher.publishEvent(new KakaoReservationProcessEvent(this, reservationDTO));
    }

    @Override
    public void publishPayEvent(PayDTO payDTO) {
        eventPublisher.publishEvent(new KakaoPayProcessEvent(this, payDTO));
    }
}