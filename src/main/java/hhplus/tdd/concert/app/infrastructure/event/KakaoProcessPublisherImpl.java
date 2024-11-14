package hhplus.tdd.concert.app.infrastructure.event;

import hhplus.tdd.concert.app.application.reservation.dto.ReservationDTO;
import hhplus.tdd.concert.app.domain.event.KakaoProcessPublisher;
import hhplus.tdd.concert.app.domain.event.KakaoProcessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoProcessPublisherImpl implements KakaoProcessPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publishEvent(ReservationDTO reservationDTO) {
        eventPublisher.publishEvent(new KakaoProcessEvent(this, reservationDTO));
    }

}
