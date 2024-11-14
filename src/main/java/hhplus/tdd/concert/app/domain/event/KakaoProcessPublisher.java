package hhplus.tdd.concert.app.domain.event;

import hhplus.tdd.concert.app.application.reservation.dto.ReservationDTO;

public interface KakaoProcessPublisher {

    void publishEvent(ReservationDTO reservationDTO) ;
}
