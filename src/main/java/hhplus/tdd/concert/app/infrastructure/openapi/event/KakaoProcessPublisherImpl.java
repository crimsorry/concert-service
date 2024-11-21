package hhplus.tdd.concert.app.infrastructure.openapi.event;

import hhplus.tdd.concert.app.application.payment.dto.PayDTO;
import hhplus.tdd.concert.app.application.reservation.dto.ReservationDTO;
import hhplus.tdd.concert.app.domain.openapi.event.KakaoPayProcessEvent;
import hhplus.tdd.concert.app.domain.openapi.event.KakaoProcessPublisher;
import hhplus.tdd.concert.app.domain.openapi.event.KakaoReservationProcessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoProcessPublisherImpl implements KakaoProcessPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String KAKAO_RESERVATION_TOTIC = "kakao_reservation_event";
    private static final String KAKAO_PAY_TOPIC = "kakao_pay_event";

    @Override
    public void publishReservationEvent(ReservationDTO reservationDTO) {
        KakaoReservationProcessEvent event = new KakaoReservationProcessEvent(reservationDTO);
        kafkaTemplate.send(KAKAO_RESERVATION_TOTIC, event);
    }

    @Override
    public void publishPayEvent(PayDTO payDTO) {
        KakaoPayProcessEvent event = new KakaoPayProcessEvent(payDTO);
        kafkaTemplate.send(KAKAO_PAY_TOPIC, event);
    }
}
