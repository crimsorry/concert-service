package hhplus.tdd.concert.app.domain.event;

import hhplus.tdd.concert.app.application.reservation.dto.ReservationDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class KakaoReservationProcessEvent extends ApplicationEvent {

    private final ReservationDTO reservationDTO;

    public KakaoReservationProcessEvent(Object source, ReservationDTO reservationDTO) {
        super(source);
        this.reservationDTO = reservationDTO;
    }

}
