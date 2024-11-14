package hhplus.tdd.concert.app.domain.event;

import hhplus.tdd.concert.app.application.payment.dto.PayDTO;
import hhplus.tdd.concert.app.application.reservation.dto.ReservationDTO;

public interface KakaoProcessPublisher {

    void publishReservationEvent(ReservationDTO reservationDTO);
    void publishPayEvent(PayDTO payDTO);
}
